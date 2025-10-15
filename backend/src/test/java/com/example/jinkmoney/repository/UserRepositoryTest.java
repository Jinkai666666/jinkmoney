package com.example.jinkmoney.repository;

import com.example.jinkmoney.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private  UserRepository userRepository;

    @Test
    public void testSaveUser(){
        User user =new User() ;
        user.setUsername("testuser");
        user.setPassword("123456");

        userRepository.save(user);
        System.out.println("保存成功，用户ID：" + user.getId());
    }
}
