package com.study.SpringSecurityMybatis.repository;

import com.study.SpringSecurityMybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
// 3.0 버전 이상인 경우 Optional 객체로 받아올 수 있음
public interface UserMapper {
    User findByUsername(String username);
    int save(User user);
}
