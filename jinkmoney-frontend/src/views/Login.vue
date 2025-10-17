<template>
  <div class="login-container">
    <h2>JinkMoney 登录 / 注册</h2>

    <el-form :model="form" label-width="70px" class="login-form">
      <el-form-item label="账号">
        <el-input v-model="form.username" placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>

      <div class="btn-group">
        <el-button type="primary" @click="login">登录</el-button>
        <el-button @click="register">注册</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import axios from 'axios'

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 登录函数
async function login() {
  try {
    const res = await axios.post('http://localhost:8080/api/user/login', form)
    // 取出后端返回的 token
    const token = res.data.data   // 后端返回的字段是 data，不是 token
    localStorage.setItem('token', token)
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

    alert('登录成功')
    setTimeout(() => {
    window.location.href = '/home'
    }, 500)
  } catch (e) {
    alert('登录失败，请检查账号或密码')
  }
}

// 注册函数
async function register() {
  try {
    await axios.post('http://localhost:8080/api/user/register', form)
    alert('注册成功，请登录')
  } catch (e) {
    alert('注册失败，账号可能已存在')
  }
}
</script>

<style scoped>
.login-container {
  width: 350px;
  margin: 150px auto;
  padding: 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 0 12px rgba(0,0,0,0.1);
}
.login-form {
  margin-top: 20px;
}
.btn-group {
  display: flex;
  justify-content: space-between;
}
</style>
