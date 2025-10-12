package com.example.jinkmoney.repository;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);
}
