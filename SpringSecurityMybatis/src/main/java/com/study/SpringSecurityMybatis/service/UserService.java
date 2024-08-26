package com.study.SpringSecurityMybatis.service;

import com.study.SpringSecurityMybatis.dto.request.ReqSignupDto;
import com.study.SpringSecurityMybatis.dto.response.RespSignupDto;
import com.study.SpringSecurityMybatis.entity.Role;
import com.study.SpringSecurityMybatis.entity.User;
import com.study.SpringSecurityMybatis.entity.UserRoles;
import com.study.SpringSecurityMybatis.repository.RoleMapper;
import com.study.SpringSecurityMybatis.repository.UserMapper;
import com.study.SpringSecurityMybatis.repository.UserRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean isDuplicateUsername(String username) {
        // Optional 객체를 만드는데 null이 가능한 상태로 만들며, User를 넣어서 만든 후 값이 있는지 없는지 비교
        // return Optional.ofNullable(userMapper.findByUsername(username)).isPresent();
        return userMapper.findByUsername(username) != null; //위 아래 같은 결과가 나오는 코드
    }

    public RespSignupDto insertUserAndUserRoles(ReqSignupDto reqDto) {
        User user = reqDto.toEntity(passwordEncoder);
        userMapper.save(user); // user 생성시 자동으로 id값을 바로 받아와서 너허주도록 세팅

        Role role = roleMapper.findByName("ROLE_USER");

        if(role == null) {
            role = Role.builder().name("ROLE_USER").build();
            roleMapper.save(role);
        }

        UserRoles userRoles = UserRoles.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build();

        userRolesMapper.save(userRoles);
        user.setUserRoles(Set.of(userRoles));

        return RespSignupDto.builder()
                .message("회원가입 완료")
                .user(user)
                .build();
    }
}
