package com.example.jinkmoney.service;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // 新增一笔交易
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // 查询所有交易
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // 删除一笔交易
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("交易记录不存在");
        }
        transactionRepository.deleteById(id);
    }

    // 修改一笔交易
    public Transaction updateTransaction(Long id, Transaction newData) {
        Transaction old = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("交易记录不存在"));
        old.setAmount(newData.getAmount());
        old.setType(newData.getType());
        old.setCategory(newData.getCategory());
        old.setNote(newData.getNote());
        old.setDate(newData.getDate());
        return transactionRepository.save(old);
    }
}
