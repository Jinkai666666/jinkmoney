package com.example.jinkmoney.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键

    @Column(nullable = false)
    private BigDecimal amount;  // 金额

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;  // 类型：收入或支出

    @Column(nullable = false)
    private String category;  // 分类：餐饮、交通、工资...

    private String note;  // 备注，可选

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();  // 交易时间，默认当前

    public enum Type {
        INCOME, EXPENSE
    }
}
