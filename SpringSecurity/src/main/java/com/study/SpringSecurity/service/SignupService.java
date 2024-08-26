package com.study.SpringSecurity.service;

import com.study.SpringSecurity.aspect.annotation.TimeAop;
import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.repository.RoleRepository;
import com.study.SpringSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignupService {

    // Autowired 대신 final 붙이고 위에 @RequiredArgsConstructor을 달아줄 수도 있음
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    // DTO에 IoC 컨테이너에 들어있는 암호화 객체를 넘겨주기 위해 의존성 주입
    private final BCryptPasswordEncoder passwordEncoder;



    @TimeAop    // 로그인 서비스 동작 시간을 잴 수 있음
    @Transactional(rollbackFor = Exception.class)  // rollbackFor 예외를 정의할 수 있음 (save 등 DB 생성 과정에서 오류가 발생할 경우 예외 처리)
    public User signup(ReqSignupDto reqDto) {
        /* User에서 바로 테이블 조인 구문을 작성 할 경우*/
        User user = reqDto.toEntity(passwordEncoder);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            return roleRepository.save(Role.builder().name("ROLE_NAME").build());
        });   // .orElseGet() <- ROLE_USER를 못 찾으면
        user.setRoles(Set.of(role));    // 중복되지 않게 set 자료형으로 데이터를 넣어줌
        return userRepository.save(user);
    }

    public boolean isDuplicatedUsername(String username) {
        // userRepository.findByUsername(username)는 Optional 객체를 리턴하기 때문에 값이 있는지 없는지만 확인
        // isPresent()는 Optional 객체의 메소드
        // Mybatis에서 isPresent()와 같은 메소드를 사용하고 싶으면 Optional 객체를 별도로 생성해서 사용하면 됨
//        passwordEncoder.matches(); // 암호화된 비밀번호가 맞는지 확인
        return userRepository.findByUsername(username).isPresent();
    }
}
