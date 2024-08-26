package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.exception.ValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice   // 컴포넌트이기 때문에 IoC 안에서 발생한 예외만 처리 가능
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidException.class)   // ()에 있는 예외가 발생하면 바로 낚아챔
    // 예외가 터지면 advice가 낚아채서 대신 응답을 해줌
    public ResponseEntity<?> validException(ValidException e) { // message, fieldErrors를 가지고 있음
        return ResponseEntity.badRequest().body(e.getFieldErrors());   // 해당 에러들을 호출하여 출력해줌
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException e) { // message, fieldErrors를 가지고 있음
//        return ResponseEntity.badRequest().body(e.getMessage());   // 해당 에러들을 호출하여 출력해줌
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage()))); // 프론트 입장에서는 메세지 하나만 전달해주면 새로운 형식을 또 만들어야함
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException e) { // message, fieldErrors를 가지고 있음
//        return ResponseEntity.badRequest().body(e.getMessage());   // 해당 에러들을 호출하여 출력해줌
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage()))); // 프론트 입장에서는 메세지 하나만 전달해주면 새로운 형식을 또 만들어야함

    }
}