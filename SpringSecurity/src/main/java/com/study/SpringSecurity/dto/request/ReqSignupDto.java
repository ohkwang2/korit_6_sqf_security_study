package com.study.SpringSecurity.dto.request;

import com.study.SpringSecurity.domain.entity.User;
import lombok.Data;

@Data
public class ReqSignupDto {
    private String username;
    private String password;
    private String checkPassword;
    private String name;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .build();
    }
}
