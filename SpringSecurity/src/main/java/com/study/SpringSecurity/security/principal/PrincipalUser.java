package com.study.SpringSecurity.security.principal;


import com.study.SpringSecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class PrincipalUser implements UserDetails { //Principal 객체가 auth..., UserDetails로 구분

    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;

    @Override
    //Role 권한들이 들어감 (return Type Collection (리스트, 셋))
    public Collection<? extends GrantedAuthority> getAuthorities() {    // GrantedAuthority를 상속받은 객체만 들어갈 수 있음

        Set<GrantedAuthority> authorities = new HashSet<>();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;

        /*위 코드를 stream을 사용할 경우*/
        /*
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
         */
    }

    @Override
    public boolean isAccountNonExpired() {  // 계정 만료 확인 (휴먼 계정 or 임시 계정)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {   // 계정이 잠겨있는지 확인 (비밀번호 5번 이상 틀리면 잠김 or 휴먼 계정)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 계정 인증이 만료
        return true;
    }

    @Override
    public boolean isEnabled() {    //활성화 유무 확인 (ex) email 인증이 남았는지 확인)
        return true;
    }
}
