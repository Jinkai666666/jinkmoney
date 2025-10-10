package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.User;
import com.example.jinkmoney.service.UserService;
import com.example.jinkmoney.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 注册接口
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // 校验账号密码
        User dbUser = userService.login(user.getUsername(), user.getPassword());
        if (dbUser != null) {
            // 登录成功 → 生成 JWT Token
            String token = jwtUtil.generateToken(dbUser.getUsername());
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return ResponseEntity.ok(data);
        } else {
            // 登录失败
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }

    // 获取当前用户信息
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("未登录或Token无效");
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(404).body("用户不存在");
        }

        // 返回安全信息（不带密码）
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());

        return ResponseEntity.ok(data);
    }
}
