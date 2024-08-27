package com.study.SpringSecurityMybatis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.SpringSecurityMybatis.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    @JsonIgnore // 이게 걸려있으면 JSON으로 응답할 때 빼줌
    private String password;
    private String name;
    private Set<UserRoles> userRoles;

    public PrincipalUser toPrincipal() {
        return PrincipalUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .roles(userRoles)
                .build();
    }
}
