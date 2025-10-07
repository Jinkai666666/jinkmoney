package com.example.jinkmoney.service;

import com.example.jinkmoney.model.User;
import com.example.jinkmoney.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 创建加密器（用于加密 & 校验密码）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 注册：加密后保存
    public User register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    // 登录：验证用户名 + 密码
    public User login(String username, String rawPassword) {
        User dbUser = userRepository.findByUsername(username);

        if (dbUser != null && passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            return dbUser; // 登录成功
        } else {
            return null; // 登录失败
        }
    }
}
