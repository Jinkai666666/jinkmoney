package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.User;
import com.example.jinkmoney.service.UserService;
import com.example.jinkmoney.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // 登录接口
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // 用 service 验证账号密码
        User dbUser = userService.login(user.getUsername(), user.getPassword());
        if (dbUser != null) {
            // 登录成功 → 生成 JWT Token
            String token = JwtUtil.generateToken(dbUser.getUsername());
            return token;
        } else {
            // 登录失败 → 返回错误提示
            return "用户名或密码错误";
        }
    }
}
