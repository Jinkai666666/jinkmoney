# JinkMoney 记账系统

JinkMoney 是一个基于 Spring Boot Vue3 Element Plus 的全栈记账系统 支持用户注册登录 账单记录 分类统计 月度与年度汇总分析

---

## 项目结构
jinkmoney
├─ backend 后端项目 Spring Boot
└─ frontend 前端项目 Vue3 Element Plus

yaml
复制代码

---

## 技术栈
### 后端 backend
Spring Boot  
Spring Security JWT  
MySQL JPA  
Maven  

### 前端 frontend
Vue3 Vite  
Element Plus  
Axios  

---

## 功能简介
用户注册 登录 JWT 鉴权  
账单记录 收入 支出  
分类统计 餐饮 交通 娱乐等  
月度收支汇总  
年度趋势图表展示  

---

## 使用方式
1 后端运行  
cd backend
mvn spring-boot:run

复制代码

2 前端运行  
cd frontend
npm install
npm run dev

复制代码

3 浏览器访问  
http://localhost:5173/

yaml
复制代码

---

## 部署说明
前端打包 npm run build  
后端打包 mvn clean package  
将打包后的前端文件放入后端 resources static 可实现整合部署  

---

## 作者
Jinkai666666  
一个持续学习与实践全栈开发的普通人  

---

## 开源协议
本项目基于 MIT License 开源