package com.example.jinkmoney.repository;

import com.example.jinkmoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
// 继承JpaRepository就能用现成的增删改查
public interface UserRepository extends JpaRepository<User, Long> {

    // 通过用户名查用户
    User findByUsername(String username);
}
