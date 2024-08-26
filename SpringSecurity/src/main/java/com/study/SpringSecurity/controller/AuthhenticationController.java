package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.aspect.annotation.ParamsAop;
import com.study.SpringSecurity.aspect.annotation.ValidAop;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.service.SigninService;
import com.study.SpringSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthhenticationController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    // Aop의 실행 순서가 필요하면 order를 걸어주어야 함.
    @ValidAop // 정규식 검증, 비밀번호 검증
    @ParamsAop
    @PostMapping("/signup")
    //  @Vaild <- 유효성 검사 요거 사용하려면 BindingResult를 사용해야 함. 정규식 검사 후 에러가 뜬 메세지들을 BindingResult에 넣어줌
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto reqDto, BeanPropertyBindingResult result) {
        // username 중복검사 서비스 호출
        return ResponseEntity.created(null).body(signupService.signup(reqDto));
    }

    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody ReqSignupDto reqDto, BeanPropertyBindingResult result) {
        return ResponseEntity.ok().body(signinService.signin(reqDto));
    }
}
