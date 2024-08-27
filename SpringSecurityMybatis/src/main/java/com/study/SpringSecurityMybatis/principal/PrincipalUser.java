package com.study.SpringSecurityMybatis.principal;


import com.study.SpringSecurityMybatis.entity.UserRoles;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class PrincipalUser implements UserDetails {

    // user정보 먼저 입력 후 ctrl + i 시 알아서 아래 메소드들 오버라이드 해줌
    private Long id;
    private String username;
    private String password;
    private Set<UserRoles> roles;

    @Override
    //role collection 만들기
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(
                ur -> new SimpleGrantedAuthority(ur.getRole().getName())    // userRole(ur) set안에 user_role_id, user_id, role_id {role_id, role_name} 요래 드가 있음
        ).collect(Collectors.toSet());
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
