package com.study.SpringSecurityMybatis.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// AuthenticationHandler는 Security에서 터진 에러들을 모아서 가져옴
// exceptionController로 던져주면 됨
@Component
public class AuthenticationHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        response.getWriter().println("인증 토큰이 유효하지 않습니다.");
        authException.printStackTrace();    // 서버쪽에서 토큰 인증 오류 내용을 볼 수 있게 해줌
    }
}
