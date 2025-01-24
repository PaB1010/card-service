package org.anonymous.member.test.annotations;


import org.anonymous.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {

    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {

        Member member = new Member();

        member.setSeq(annotation.seq());
        member.setName(annotation.name());
        member.setEmail(annotation.email());

        member.set_authorities(Arrays.stream(annotation.authority()).toList());

        // Authentication interface 구현 객체
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

        // 새로운 Context 객체(인증한 사용자에 대한 정보 객체)
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 로그인 처리
        // MemberUtils Class 의 getMember 메서드 내부 참고
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        context.setAuthentication(authentication);

        return context;
    }
}