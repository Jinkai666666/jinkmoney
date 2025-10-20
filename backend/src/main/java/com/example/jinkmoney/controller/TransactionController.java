package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.repository.TransactionRepository;
import com.example.jinkmoney.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 账单接口：新增、查询、月汇总、年汇总
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 通用方法：从请求头提取 userId（自动校验 token）
     */
    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }

    /**
     * 添加账单（自动绑定当前登录用户）
     */
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {
        Long userId = extractUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Token 无效或已过期");

        transaction.setUserId(userId);
        transactionRepository.save(transaction);
        return ResponseEntity.ok("账单添加成功");
    }

    /**
     * 获取当前用户账单列表（支持按月份筛选）
     */
    @GetMapping("/list")
    public ResponseEntity<?> getUserTransactions(@RequestParam(required = false) String month,
                                                 HttpServletRequest request) {
        Long userId = extractUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Token 无效或已过期");

        List<Transaction> transactions = (month != null && !month.isEmpty())
                ? transactionRepository.findByUserIdAndMonth(userId, month)
                : transactionRepository.findByUserId(userId);

        return ResponseEntity.ok(transactions);
    }

    /**
     * 月度汇总（收入、支出、结余）
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getMonthlySummary(@RequestParam String month,
                                               HttpServletRequest request) {
        Long userId = extractUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Token 无效或已过期");

        Double income = Optional.ofNullable(transactionRepository.getMonthlyIncome(userId, month)).orElse(0.0);
        Double expense = Optional.ofNullable(transactionRepository.getMonthlyExpense(userId, month)).orElse(0.0);

        return ResponseEntity.ok(Map.of(
                "income", income,
                "expense", -expense,  // 转正
                "balance", income + expense
        ));
    }

    /**
     * 年度汇总（每月收入支出趋势）
     */
    @GetMapping("/year-summary")
    public ResponseEntity<?> getYearSummary(@RequestParam String year,
                                            HttpServletRequest request) {
        Long userId = extractUserId(request);
        if (userId == null) return ResponseEntity.status(401).body("Token 无效或已过期");

        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String month = String.format("%s-%02d", year, i);
            Double income = Optional.ofNullable(transactionRepository.getMonthlyIncome(userId, month)).orElse(0.0);
            Double expense = Optional.ofNullable(transactionRepository.getMonthlyExpense(userId, month)).orElse(0.0);

            result.add(Map.of(
                    "month", month,
                    "income", income,
                    "expense", -expense,
                    "balance", income + expense
            ));
        }
        return ResponseEntity.ok(result);
    }
}
