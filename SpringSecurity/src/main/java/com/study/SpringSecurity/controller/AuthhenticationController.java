package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthhenticationController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody ReqSignupDto reqDto) {
        log.info("{}", reqDto);
        return ResponseEntity.created(null).body(signupService.signup(reqDto));
    }
}
