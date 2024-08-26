package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// AOP를 사용하기 위한 기본 틀
@Aspect
@Component
@Order(value = 1)
public class TestAspect2 {

    // 해당 어노테이션이 있는 곳을 찾아서 잘라옴
    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.Test2Aop)")  // 여기 오타가 하나라도 있으면 compile이 안됨.
    private void pointCut() {}

    // 공통 사항이 있으면 위에 Point 컷을 여러개 설정해놓고 Around에 &로 여러개를 등록 가능함
    // 하지만, 보통은 그렇게 안 쓰고 어노테이션을 달아서 사용 (? 이기 무슨 말이지?)
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // proceedingJoinPoint.getArgs(); // .getArgs <- 요거의 return type은 Object 배열 (매개변수로 넣어준 값들이 들어가 있음)


        // 시그니처 객체를 리턴해줌 (원래 proceedingJoinPoint는 CodeSignature를 Signature로 upcasting 되서 리턴해줌)
        // CodeSignature로 다운캐스팅 해서 사용할 경우 더 다양한 메소드를 사용할 수 있음
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        System.out.println(signature.getName()); // 메소드명 출력
        System.out.println(signature.getDeclaringTypeName());   // 해당 메소드가 있는 클래스명 출력
        String[] paramNames = signature.getParameterNames();
        Object[] args = proceedingJoinPoint.getArgs();
        for(int i = 0; i < args.length; i++) {
            System.out.println(paramNames[i] + ": " + args[i]);
        }

        System.out.println("전처리2");
        Object result = proceedingJoinPoint.proceed(); // 핵심 기능 호출
        System.out.println("후처리2");

        return result;
    }

}
