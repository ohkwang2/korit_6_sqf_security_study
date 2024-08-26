package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// AOP를 사용하기 위한 기본 틀
@Aspect
@Component
@Order(value = 2)
public class TestAspect {

    // 기본적으로 접근지정자를 생략하면 public임 (모두 가져오고 싶으면 "*")
    // 경로 앞에는 가져오는 자료의 리턴 자료형을 입력 (모두 가져오고 싶으면 "*")
    // 최종 메소드 이름에 중간 생략하고 *을 입력하면 앞글자로 시작하는 모든 메소드를 불러옴
    // 매개변수 자리에 (..) <- ..이 2개이면 매개변수 0개 이상 즉, 매개변수가 있어도 되고 없어도 된다.
    // 하위에 지정한 것은 리턴이 String 자료형이며, ...경로에 있는 aop로 시작하는 이름을 가진 메소드에 대해 매개변수 개수와 상관없이 모두 지정
    @Pointcut("execution(* com.study.SpringSecurity.service.TestService.aop*(..))")  // 여기 오타가 하나라도 있으면 compile이 안됨.
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("전처리");
        Object result = proceedingJoinPoint.proceed(); // 핵심 기능 호출
        System.out.println("후처리");

        return result;
    }
}
