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
 * 账单接口：新增账单、查看自己账单（可按月份筛选）
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

    // 获取当前用户的账单列表（支持按月份筛选）
    @GetMapping("/list")
    public ResponseEntity<?> getUserTransactions(
            @RequestParam(required = false) String month,
            HttpServletRequest request) {

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

        // 如果带了 ?month=2025-10 参数，就查这个月的账单
        List<Transaction> transactions;
        if (month != null && !month.isEmpty()) {
            transactions = transactionRepository.findByUserIdAndMonth(userId, month);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        return ResponseEntity.ok(transactions);
    }
    //月筛选，收入支出
    @GetMapping("/summary")
    public ResponseEntity<?> getMonthlySummary(
            @RequestParam String month,
            HttpServletRequest request) {

        // 取 token，解析 userId
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        // 计算收入和支出
        Double income = transactionRepository.getMonthlyIncome(userId, month);
        Double expense = transactionRepository.getMonthlyExpense(userId, month);

        // 防止 null
        if (income == null) income = 0.0;
        if (expense == null) expense = 0.0;

        // 计算结余
        Double balance = income + expense; // 因为支出是负数

        // 返回结果
        return ResponseEntity.ok(
                java.util.Map.of(
                        "income", income,
                        "expense", -expense,  // 转正数给前端
                        "balance", balance
                )
        );
    }


    @GetMapping("/year-summary")
    public ResponseEntity<?> getYearSummary(
            @RequestParam String year,
            HttpServletRequest request) {

        // 取 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("缺少或无效的 Token");
        }

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Token 无效或已过期");
        }

        // 构造返回数据
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String month = String.format("%s-%02d", year, i); // 2025-01, 2025-02 ...
            Double income = transactionRepository.getMonthlyIncome(userId, month);
            Double expense = transactionRepository.getMonthlyExpense(userId, month);

            if (income == null) income = 0.0;
            if (expense == null) expense = 0.0;

            double balance = income + expense; // expense 是负数

            result.add(java.util.Map.of(
                    "month", month,
                    "income", income,
                    "expense", -expense, // 转为正数
                    "balance", balance
            ));
        }

        return ResponseEntity.ok(result);
    }


}
