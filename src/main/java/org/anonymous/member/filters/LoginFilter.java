package org.anonymous.member.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.anonymous.member.Member;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFilter extends GenericFilterBean {

    private final Utils utils;

    private final ObjectMapper om;

    private final RestTemplate restTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = utils.getAuthToken();

        loginProcess(token);

        chain.doFilter(request, response);
    }

    /**
     * 로그인 처리
     *
     * @param token
     */
    private void loginProcess(String token) {

        // 토큰이 없는 일반 요청인 경우는 처리 X
        if (!StringUtils.hasText(token)) return;

        /**
         * 1. 토큰이 있으면 member-service 인스턴스에서 회원 정보 조회
         * 2. 로그인 처리
         */
        try {

            String apiUrl = utils.serviceUrl("member-service", "/");

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<JSONData> response = restTemplate.exchange(apiUrl, HttpMethod.GET, request, JSONData.class);

            JSONData jsonData = response.getBody();
            if (response.getStatusCode().is2xxSuccessful() && jsonData != null && jsonData.isSuccess()) { // 응답 성공시 처리
                String json = om.writeValueAsString(jsonData.getData());

                Member member = om.readValue(json, Member.class);

                Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } // endif


        } catch (Exception e) { e.printStackTrace(); }
    }
}