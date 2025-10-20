package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.repository.TransactionRepository;
import com.example.jinkmoney.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        transaction.setUserId(userId);

        // 防止前端没传 type 报错
        if (transaction.getType() == null) {
            return ResponseEntity.badRequest().body("缺少 type（INCOME / EXPENSE）");
        }

        transactionRepository.save(transaction);
        return ResponseEntity.ok("账单添加成功");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUserTransactions(
            @RequestParam(required = false) String month,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        List<Transaction> list = (month != null && !month.isEmpty())
                ? transactionRepository.findByUserIdAndMonth(userId, month)
                : transactionRepository.findByUserId(userId);

        return ResponseEntity.ok(list);
    }
}
