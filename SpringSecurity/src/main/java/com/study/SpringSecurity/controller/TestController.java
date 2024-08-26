package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import com.study.SpringSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {
//        System.out.println(testService.aopTest());  // 해당 결과는 "전처리" -> "AOP 테스트입니다." -> "후처리" -> "AOP 테스트입니다."
//        testService.aopTest2("김준일", 31);
//        testService.aopTest3("010-1234-1234", "부산시 동래구");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();    // UsenamePasswordAuthenticationToken에서 Object로 업캐스팅 되어 set 되어 있음.

        return ResponseEntity.ok(principalUser);
    }
}
