package org.anonymous.global.configs;

import lombok.RequiredArgsConstructor;
import org.anonymous.member.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Lazy
@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MemberUtil memberUtil;

    @Override
    public Optional<String> getCurrentAuditor() {

        String email = null;
        if (memberUtil.isLogin()) {
            email = memberUtil.getMember().getEmail();
        }

        return Optional.ofNullable(email);
    }
}
