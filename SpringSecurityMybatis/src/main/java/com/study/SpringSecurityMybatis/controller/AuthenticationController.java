package com.study.SpringSecurityMybatis.controller;

import com.study.SpringSecurityMybatis.dto.request.ReqSignupDto;
import com.study.SpringSecurityMybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto reqDto, BeanPropertyBindingResult bindingResult) {
        if(!reqDto.getPassword().equals(reqDto.getCheckPassword())) {
            FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
            bindingResult.addError(fieldError);
        }

        if(userService.isDuplicateUsername(reqDto.getUsername())) {
            FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다.");
            bindingResult.addError(fieldError);
        }

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError());
        }
        return ResponseEntity.created(null).body(userService.insertUserAndUserRoles(reqDto));
    }
}
