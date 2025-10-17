package com.example.jinkmoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JinkmoneyApplication {
    public static void main(String[] args) {
        System.out.println("=== 当前工作目录 === " + System.getProperty("user.dir"));
        SpringApplication.run(JinkmoneyApplication.class, args);
    }
}

