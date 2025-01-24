package org.anonymous.global.libs;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.member.MemberUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Lazy
@Component
@RequiredArgsConstructor
public class Utils {

    private final MemberUtil memberUtil;

    private final HttpServletRequest request;

    private final MessageSource messageSource;

    private final DiscoveryClient discoveryClient;

    /**
     * 메세지 코드로 조회된 문구
     *
     * @param code
     * @return
     */
    public String getMessage(String code) {

        // 요청 header 에 있는 언어 정보(Accept-Language)로 만들어지는 Locale 객체
        Locale lo = request.getLocale();

        return messageSource.getMessage(code, null, lo);
    }

    /**
     * 메세지 코드를 배열로 받았을때
     * List 로 변환해 반환해주는 기능
     *
     * @param codes
     * @return
     */
    public List<String> getMessages(String[] codes) {

        return Arrays.stream(codes).map(c -> {

            try {
                return getMessage(c);

            } catch (Exception e) {
                // ★ 예외 발생시 빈 문자열로 교체하는 방식으로 제거 ★
                return "";
            }
            // 비어있지 않은 문자열, 즉 코드만 걸러서 가져옴
        }).filter(s -> !s.isBlank()).toList();
    }


    /**
     * REST 커맨드 객체 검증 실패시에
     * Error Code 에서 Message 추출하는 기능
     *
     * @param errors
     * @return
     */
    public Map<String, List<String>> getErrorMessages(Errors errors) {

        // 형변환해도 싱글톤 객체
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
            // 필드별 Error Code - getFieldErrors()
            // FieldError = 커맨드 객체 검증 실패 & rejectValue(..)
            // Collectors.toMap = (Key = 필드명, Value = 메세지)
            Map<String, List<String>> messages = errors.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, f -> getMessages(f.getCodes()), (v1, v2) -> v2));
            // v1 = 처음 값, v2 = 마지막에 들어온 값
            // -> 중복될 경우 마지막 값으로 대체되도록 처리 (put과 유사)

            // 글로벌 Error Code - getGlobalErrors()
            // GlobalError = reject(..)
            List<String> gMessages = errors.getGlobalErrors()
                    .stream()
                    // flatMap = 중첩된 stream() 펼쳐서 1차원 배열로 변환
                    .flatMap(o -> getMessages(o.getCodes()).stream())
                    .toList();

            // Global ErrorCode Field = "global" 으로 임의 고정
            if (!gMessages.isEmpty()) {

                messages.put("global", gMessages);
            }
            return messages;
    }

    /**
     * 유레카 서버 인스턴스 주소 검색
     *
     *      spring.profiles.active : dev - localhost 로 되어있는 주소 반환
     *          - EX) member-service : 최대 두가지만 존재
     *                                  1) 실 서비스 도메인 주소 2) localhost... (개발용)
     * @param serviceId
     * @param url
     * @return
     */
    public String serviceUrl(String serviceId, String url) {

        try {

            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

            String profile = System.getenv("spring.profiles.active");

            // 개발 모드 - localhost 의 Service Url
            boolean isDev = StringUtils.hasText(profile) && profile.contains("dev");

            String serviceUrl = null;

            for (ServiceInstance instance : instances) {

                String uri = instance.getUri().toString().toString();
                if (isDev && uri.contains("localhost")) serviceUrl = uri;

                else if (!isDev && !uri.contains("localhost")) serviceUrl = uri;
            }

            if (StringUtils.hasText(serviceUrl)) {

                return serviceUrl + url;
            }
        } catch (Exception e) { e.printStackTrace();}

        return "";
    }

    /**
     * 요청 헤더 : Authorization: Bearer ...
     *
     * @return
     */
    public String getAuthToken() {

        String auth = request.getHeader("Authorization");

        return StringUtils.hasText(auth) ? auth.substring(7).trim() : null;
    }

    /**
     * 전체 주소
     *
     * @param url
     * @return
     */
    public String getUrl(String url) {

        int port = request.getServerPort();

        String _port = port == 80 || port == 443 ? "" : ":" + port;

        return String.format("%s://%s%s%s%s",request.getScheme(), request.getServerName(), _port, request.getContextPath(), url);
    }

    /**
     * 사용자별로 구분할 수 있는 Cookie
     *
     * @return
     */
    public String getUserHash() {

        String userKey = "" + Objects.hash("userHash");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("userHash")) {

                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    // Browser 정보 Mobile 여부 확인
    public boolean isMobile() {

        // 요청 header -> User-Agent (Browser 정보)
        // ★ iPhone / Android 판별도 가능,
        // 어느 층의 User 가 더 많은지 판단해 App 개발에 활용 ★
        String ua = request.getHeader("User-Agent");

        // 해당 Pattern 이 포함되면 Mobile 판단
        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return StringUtils.hasText(ua) && ua.matches(pattern);
    }

    /**
     * 요청 헤더 get 편의 기능
     *
     * - JWT 토큰이 있으면 자동 추가
     *
     * @return
     */
    public HttpHeaders getRequestHeader() {

        String token = getAuthToken();

        HttpHeaders headers = new HttpHeaders();

        if (StringUtils.hasText(token)) {

            headers.setBearerAuth(token);
        }

        return headers;
    }

    /**
     * 회원 / 비회원 구분 해시 (int)
     *
     * 회원 - 회원번호 / 비회원 - (IP + User-Agent)
     *
     * @return
     */
    public int getMemberHash() {

        // 회원
        if (memberUtil.isLogin()) return Objects.hash(memberUtil.getMember().getSeq());

        else { // 비회원

            String ip = request.getRemoteAddr();

            String ua = request.getHeader("User-Agent");

            return Objects.hash(ip, ua);
        }
    }
}