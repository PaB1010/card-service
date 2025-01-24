package org.anonymous.member;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.anonymous.member.constants.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements UserDetails {

    private Long seq;

    private String email;

    private String name;

    @JsonAlias("authorities")
    private List<Authority> _authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return _authorities == null || _authorities.isEmpty()
                ? List.of()
                : _authorities.stream().map( s -> new SimpleGrantedAuthority(s.name())).toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}