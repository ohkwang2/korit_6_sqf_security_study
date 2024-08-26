package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 프로그램 실행 중일 때 동작
@Target({ElementType.METHOD})   // 메소드 위에만 해당 어노테이션을 달 수 있음 (클래스, 변수도 각각 설정 가능)
public @interface Test2Aop {

}
