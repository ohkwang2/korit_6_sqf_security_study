package com.study.SpringSecurity.domain.entity;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Entity 어노테이션을 다는 순간부터 해당 클래스는 테이블이 됨
@Entity
@Builder
@Data
@NoArgsConstructor // Builder 때문에 빈 생성자가 사라짐 (JPA 사용할 때 빈 생성자 필요함)
@AllArgsConstructor // Builder 때문에 빈 생성자가 사라짐
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

    // fetch: 엔티티 조인했을 때 연관된 데이터를 언제 가져올지 결정(EAGER - 당장, LAZY - 나중에 사용할 때)
    // cascade: 부모를 INSERT, UPDATE, DELETE 할 때 참조하는 애들도 다 같이 지워져야 함을 의미

    /* User에서 바로 테이블 조인 구문을 작성 할 경우*/
    // ManyToMany를 사용하려면 Join을 해야함
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // JoinTable 기준으로 테이블 조인
    @JoinTable(
            name = "user_roles",    // user_roles라는 테이블 명
            // JPA에서 초기에 tb 생성시 ID는 엔티티 클래스명_id로 만들어 줌, user_id가 같은 것 끼리 join
            joinColumns = @JoinColumn(name = "user_id"), // user_roles 테이블의 키값 (조인 대상 키값)
            inverseJoinColumns = @JoinColumn(name = "role_id")  // 참조되는 키값
    )
    // Security 사용을 위해 권한은 여러개를 가질 수 있어야 함
    private Set<Role> roles;   // 단 여기서 조인은 다 : 다 조인이기 때문에 중복 제거가 필요함

    public PrincipalUser toPrincipalUser() { //인증된 user 정보를 PrincipalUser로 변환하여 저장
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

}
