package com.study.SpringSecurity.init;

import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {  // 프로그램이 실행되면 자동으로 실행되는 클래스 ??

    @Autowired
    private RoleRepository roleRepository;

    @Override
    // (String... args) <- 개수 상관없이 여러개를 배열 형태로 받아옴. 대신 사용할때는 뽑아서 써야 함.
    public void run(String... args) throws Exception {
        // role 테이블에 ROLE_USER라는 이름이 없으면 TRUE
        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }
        if(roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_MANAGER").build());
        }

    }
}
