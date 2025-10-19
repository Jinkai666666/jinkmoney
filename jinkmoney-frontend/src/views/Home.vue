<template>
  <el-container style="height: 100vh;">
    <!-- 顶部导航栏 -->
    <el-header style="background-color: #409EFF; color: white; display: flex; justify-content: space-between; align-items: center;">
      <div style="font-size: 20px; font-weight: bold;">JinkMoney 记账系统</div>
      <div>
        <span style="margin-right: 15px;">欢迎，{{ username }}</span>
        <el-button type="danger" size="small" @click="logout">退出登录</el-button>
      </div>
    </el-header>

    <!-- 主体区域 -->
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside width="200px" style="background-color: #f5f7fa;">
        <el-menu :default-active="activeMenu" @select="handleMenuSelect">
          <el-menu-item index="record">账单记录</el-menu-item>
          <el-menu-item index="stats">统计报表</el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 内容显示区 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = localStorage.getItem('username') || '用户'
const activeMenu = ref('record')

function handleMenuSelect(key) {
  activeMenu.value = key
  router.push(`/${key}`)
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>
