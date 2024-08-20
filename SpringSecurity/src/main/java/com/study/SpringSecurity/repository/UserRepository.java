package com.study.SpringSecurity.repository;

import com.study.SpringSecurity.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 원래 Ioc 컨테이너에 등록하려면 class여야 가능한데, JPA라서 가능
@Repository
public interface UserRepository extends JpaRepository<User, Long> { // Jpa 클래스가 제네릭이라서 <엔티티 클래스, id 타입> 입력 필요

}
