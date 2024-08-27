package com.study.SpringSecurityMybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {

    private Long id;
    private Long userId;
    private Long roleId;
    private Role role;  // 조인 될 때 roleId는 1:1 매칭
}
