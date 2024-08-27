package com.study.SpringSecurityMybatis.service;

import com.study.SpringSecurityMybatis.dto.request.ReqSigninDto;
import com.study.SpringSecurityMybatis.dto.request.ReqSignupDto;
import com.study.SpringSecurityMybatis.dto.response.RespDeleteUserDto;
import com.study.SpringSecurityMybatis.dto.response.RespSignupDto;
import com.study.SpringSecurityMybatis.dto.response.ResponseSigninDto;
import com.study.SpringSecurityMybatis.entity.Role;
import com.study.SpringSecurityMybatis.entity.User;
import com.study.SpringSecurityMybatis.entity.UserRoles;
import com.study.SpringSecurityMybatis.exception.DeleteUserException;
import com.study.SpringSecurityMybatis.exception.SignupException;
import com.study.SpringSecurityMybatis.principal.PrincipalUser;
import com.study.SpringSecurityMybatis.repository.RoleMapper;
import com.study.SpringSecurityMybatis.repository.UserMapper;
import com.study.SpringSecurityMybatis.repository.UserRolesMapper;
import com.study.SpringSecurityMybatis.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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

    @Autowired
    private JwtProvider jwtProvider;

    public boolean isDuplicateUsername(String username) {
        // Optional 객체를 만드는데 null이 가능한 상태로 만들며, User를 넣어서 만든 후 값이 있는지 없는지 비교
        // return Optional.ofNullable(userMapper.findByUsername(username)).isPresent();
        return userMapper.findByUsername(username) != null; //위 아래 같은 결과가 나오는 코드
    }

    // 여기 3번의 세이브가 있는데, 하나라도 예외가 터지면 rollback 해주어야 함
    @Transactional(rollbackFor = SignupException.class)   // 하나라도 예외가 터지면 롤백을 처리
    public RespSignupDto insertUserAndUserRoles(ReqSignupDto reqDto) throws SignupException{
        User user = null;
        try{
            user = reqDto.toEntity(passwordEncoder);
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
        } catch (Exception e) {
            throw new SignupException(e.getMessage());
        }

        return RespSignupDto.builder()
                .message("회원가입 완료")
                .user(user)
                .build();
    }

    public ResponseSigninDto getGeneratedAccessToken(ReqSigninDto reqDto) {
        User user = checkUsernameAndPassword(reqDto.getUsername(), reqDto.getPassword());

        return ResponseSigninDto.builder()
                .expireDate(jwtProvider.getExpireDate().toLocaleString())
                .accessToken(jwtProvider.generateAccessToken(user))
                .build();
    }

    private User checkUsernameAndPassword(String username, String password) {
        User user = userMapper.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("사용자 정보를 다시 확인하세요.");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 확인하세요.");
        }

        return user;
    }

    @Transactional(rollbackFor = SQLException.class)    // 다른 내용은 예외를 터트려서 날리고, 나머지는 SQL에서 나오는 오류 처리
    public RespDeleteUserDto deleteUser(Long userId) {
        User user = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();    // Object 객체 다운캐스팅
            if(principalUser.getId() != userId) {
                throw new AuthenticationServiceException("삭제할 수 있는 권한이 없습니다."); // ID가 달라서 삭제할 수 있는 권한이 없음
            }
            user = userMapper.findByUserId(userId);
            if(user == null) {
                throw new AuthenticationServiceException("해당 사용자는 존재하지 않는 사용자입니다.");
            }
            userRolesMapper.deleteByUserId(userId);
            userMapper.deleteByUserId(userId);

        return RespDeleteUserDto.builder()
                .isDeleting(true)
                .message("사용자 삭제 완료")
                .deletedUser(user)
                .build();
    }
}
