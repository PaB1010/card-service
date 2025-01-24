package org.anonymous.member;

import org.anonymous.member.constants.Authority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberUtil {

    // 로그인 상태 여부 체크
    public boolean isLogin() {

        return getMember() != null;
    }

    // 관리자 여부 체크
    public boolean isAdmin() {

        return isLogin() && getMember().get_authorities().stream().anyMatch(a -> a == Authority.ADMIN);
    }

    // 현재 로그인한 회원 정보
    public Member getMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Member) {

            return (Member) authentication.getPrincipal();
        }

        return null;
    }
}