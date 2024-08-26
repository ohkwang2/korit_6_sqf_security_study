package com.study.SpringSecurity.service;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.dto.response.RespJwtDto;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSignupDto reqDto) {
        // username 틀렸을 때
        User user = userRepository.findByUsername(reqDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        );
        // password 틀렸을 때
        if(!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }

        System.out.println("로그인 성공");

        return RespJwtDto.builder()
                .accessToken(jwtProvider.generateUserToken(user))
                .build(); // user 정보를 기반으로 토큰을 생성해서 리턴됨
    }
}
