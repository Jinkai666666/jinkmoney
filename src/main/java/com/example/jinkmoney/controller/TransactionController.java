package com.example.jinkmoney.controller;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // 新增一笔账单
    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(saved);
    }

    // 查询所有账单
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> list = transactionService.getAllTransactions();
        return ResponseEntity.ok(list);
    }

    // 修改账单
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction transaction) {
        Transaction updated = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updated);
    }

    // 删除账单
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("删除成功");
    }
}
