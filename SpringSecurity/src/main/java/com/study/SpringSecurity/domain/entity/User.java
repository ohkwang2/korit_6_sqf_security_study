package com.study.SpringSecurity.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

// Entity 어노테이션을 다는 순간부터 해당 클래스는 테이블이 됨
@Entity
@Data
@Builder
public class User {

    @Id //PK를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SQL에서 AUTO INCREMENT를 의미
    private Long id;    // id는 다른 변수명을 못 쓰고 꼭 'id'라고 써야 함.
    @Column(unique = true, nullable = false)  // 컬럼의 속성을 UNIQUE, NOT NUll로 만들어줌
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
}
