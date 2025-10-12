package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.repository.TransactionRepository;
import com.example.jinkmoney.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账单接口：新增账单、查看自己账单
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 添加账单（自动绑定当前登录用户）
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {
        // 从请求头拿 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        // 去掉前缀“Bearer ”
        String token = authHeader.replace("Bearer ", "");

        // 从 token 里拿出 userId
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        // 把 userId 绑定到账单上
        transaction.setUserId(userId);

        // 保存到账单表
        transactionRepository.save(transaction);

        return ResponseEntity.ok("账单添加成功");
    }

    // 获取当前用户的账单列表
    @GetMapping("/list")
    public ResponseEntity<?> getUserTransactions(HttpServletRequest request) {
        // 拿 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        // 拿 userId
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        // 按 userId 查账单
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        // 返回结果
        return ResponseEntity.ok(transactions);
    }
}
