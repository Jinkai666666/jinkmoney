package com.example.jinkmoney.model;

import jakarta.persistence.*;
import lombok.Data;

// 实体类，对应数据库表
@Entity
@Table(name = "users") // 表名
@Data // Lombok 自动生成 getter/setter/toString/hashCode
public class User {

    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Long id;  // 用户id

    //用户名
    //不能为空  不能重复
    @Column(nullable = false,unique = true)
    private String username;
    // 密码
    @Column(nullable = false)
    private String password;

}
