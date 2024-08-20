package com.study.SpringSecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> get() {
        System.out.println("요청 들어옴???");
        return ResponseEntity.ok("확인");
    }
}
