package com.study.SpringSecurity.aspect;

import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.exception.ValidException;
import com.study.SpringSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Slf4j
@Aspect
@Component
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.ValidAop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {    // ProceedingJoinPoint <- 실행하고자 하는 핵심 로직을 가져옴 (signup 메소드)
        // signup(..) 메소드의 모든 값들을 가져와서 object 배열에 넣어줌
        // 그 중에서 BidingResult가 구현된 객체는 BeanPropertyBindingResult에 들어있음
        Object[] args = proceedingJoinPoint.getArgs();

        // args의 어떤게 bindingResult인지 모르니 반복을 돌며 찾아야 함
        BeanPropertyBindingResult bindingResult = null;
//        System.out.println("BeanPropertyBindingResult class가 무야? : " + BeanPropertyBindingResult.class);
//        System.out.println("BindingResult class가 무야? : " + BindingResult.class);
        // BeanPropertyBindingResult가 BindingResult를 implement 하는 인터페이스
        for(Object arg : args) {
            // 모든 객체는 .getClass() 메소드가 들어있음.
            // .getClass()를 호출하면 해당 객체의 원 데이터 타입이 출력됨
//            System.out.println(arg.getClass().getSimpleName());
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                // args는 Object 객체이기 때문에 BeanPropertyBindingResult로 다운캐스팅 해서 비교해야 함
                bindingResult = (BeanPropertyBindingResult) arg; // Object로 들어가있는 arg를 다운캐스팅 해서 bindingResult에 넣어줌
                break;
            }
        }

        switch(proceedingJoinPoint.getSignature().getName()) {
            case "signup" :
                validSignupDto(args, bindingResult);    // 비밀번호 유효성 검증 함수 호출
                break;
        }

        if(bindingResult.hasErrors()) {
            // 예외 터지면 아래 return으로 넘어가지 않음. (그럼 500에러가 뜨는데, 클라이언트가 잘못한것이기 때문에 500에러로 처리가 되면 안 됨)
            // 예외 터지면 끌어갈 수 있게 advice를 생성해야 함
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors());
        }

        Object result = proceedingJoinPoint.proceed();

        return result;
    }

    private void validSignupDto(Object [] args, BeanPropertyBindingResult bindingResult) {  // 모듈화를 위해 기능을 빼서 놓음
        for(Object arg : args) {    // 조건이 없으면 회원가입이 아니더라도 valid AOP를 실행하도록 연결되어 있는 클래스가 호출되면 실행됨
            if(arg.getClass() == ReqSignupDto.class) {
                ReqSignupDto dto = (ReqSignupDto) arg;
                if(!dto.getPassword().equals(dto.getCheckPassword())) {
                    // default로 ObjectName, feildName, message를 받을 수 있음
                    FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError);
                }
                if(signupService.isDuplicatedUsername(dto.getUsername())) {
                    FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다.");
                    bindingResult.addError(fieldError);
                }
                break;
            }
        }
    }
}
