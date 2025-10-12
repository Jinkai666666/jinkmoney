package com.example.jinkmoney.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction 实体类 —— 账单记录表
 * 功能：保存每条账单的详细信息（收入/支出、金额、分类、时间、备注、所属用户）
 */
@Entity
@Table(name = "transactions") // 注意加“s”避免与SQL保留字冲突
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键

    @Column(nullable = false)
    private Long userId;  // 用户外键

    @Column(nullable = false)
    private BigDecimal amount;  // 金额（精确小数）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;  // 类型：INCOME（收入）或 EXPENSE（支出）

    @Column(nullable = false)
    private String category;  // 分类

    private String note;  // 备注，可选

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();  // 创建时间，默认当前时间

    public enum Type {
        INCOME, EXPENSE
    }
}
