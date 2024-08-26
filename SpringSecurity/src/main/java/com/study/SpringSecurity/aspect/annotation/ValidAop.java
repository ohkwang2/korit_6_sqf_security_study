package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE}) //.TYPE은 TYPE위에 쓸 수 있다 (즉, 클래스 위에도 사용 가능)
public @interface ValidAop {
}
