package com.study.SpringSecurity.security.filter;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String bearerAccessToken = request.getHeader("Authorization");    // 요청시 헤더에서 토큰 값을 가져옴 (header의 key값은 "Authorization")
//        System.out.println(bearerAccessToken);
        if(bearerAccessToken != null) { // accessToken이 null이면 subString에서 오류 발생
            String accessToken = jwtProvider.removeBearer(bearerAccessToken);
            // 토큰이 null은 아니지만, 유효하지 않을 경우가 있기 때문에 예외처리 해주어야 함
            Claims claims = null;
            try{
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse);  // 응답 후 다시 필터로 와서 응답 후 다시 돌지 않도록 return 걸어주어야 함
                return;
            }

            Long userId = ((Integer) claims.get("userId")).longValue(); // int 타입을 Integer로 형변환 후 그 데이터를 .longValue()로 변환해주어야 함
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {    // db에서 userId를 찾았으나 못 들고 왔음. 즉, 토큰은 존재하지만 DB에 계정이 없는 경우
                filterChain.doFilter(servletRequest, servletResponse);  // 응답 후 다시 필터로 와서 응답 후 다시 돌지 않도록 return 걸어주어야 함
                return;
            }

            PrincipalUser principalUser = optionalUser.get().toPrincipalUser(); // null이 아닌 user 정보를 principalUser 객체로 변형하여 저장
            //서명 값 (비밀번호 넣어주어야 함 (principal 객체, pw, 권한정보들)
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());

            // System.out.println("예외 발생하지 않음");
            // 인증의 최종 목표
            // set 되서 setAuthentication 객체가 들어가 있어야 인증(로그인) 되었다고 봄
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 위는 전처리
        filterChain.doFilter(servletRequest, servletResponse);
        // 아래는 후처리
    }
}
