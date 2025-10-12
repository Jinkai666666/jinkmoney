package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.User;
import com.example.jinkmoney.service.UserService;
import com.example.jinkmoney.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户接口：注册、登录、获取当前用户信息
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 注册
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // 直接调用 service 保存用户
        return userService.register(user);
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // 校验账号密码
        User dbUser = userService.login(user.getUsername(), user.getPassword());
        if (dbUser == null) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

        // 登录成功 → 生成 token（存 userId）
        String token = jwtUtil.generateToken(dbUser.getId());

        // 返回给前端
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return ResponseEntity.ok(data);
    }

    // 获取当前登录用户信息
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        // 检查 token 是否存在
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        // 去掉前缀
        String token = authHeader.replace("Bearer ", "");

        // 提取 userId
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        // 根据 userId 查用户
        User user = userService.findById(userId);
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
