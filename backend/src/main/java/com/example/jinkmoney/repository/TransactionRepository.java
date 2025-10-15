package com.example.jinkmoney.repository;

import com.example.jinkmoney.model.Transaction;
import com.example.jinkmoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //查id
    List<Transaction> findByUserId(Long userId);

   //查日期
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :month")
    List<Transaction> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);
    //月收入
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.userId = :userId AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :month AND t.amount > 0")
    Double getMonthlyIncome(@Param("userId") Long userId, @Param("month") String month);
    //月支出
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.userId = :userId AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :month AND t.amount < 0")
    Double getMonthlyExpense(@Param("userId") Long userId, @Param("month") String month);

}
