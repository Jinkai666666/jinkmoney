<template>
  <el-container style="height: 100vh;">
    <!-- 顶部导航栏 -->
    <el-header
      style="background-color: #409EFF; color: white; display: flex; justify-content: space-between; align-items: center;"
    >
      <div style="font-size: 20px; font-weight: bold;">JinkMoney 记账系统</div>

      <div>
        <span style="margin-right: 15px;">欢迎，{{ username }}</span>
        <el-button type="danger" size="small" @click="logout">退出登录</el-button>
      </div>
    </el-header>

    <!-- 主体部分 -->
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside width="200px" style="background-color: #f5f7fa;">
        <el-menu
          :default-active="activeMenu"
          @select="handleMenuSelect"
          style="height: 100%;"
        >
          <el-menu-item index="record">账单记录</el-menu-item>
          <el-menu-item index="stats">统计报表</el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 内容区域 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 用户名
const username = localStorage.getItem('username') || '用户'

// 当前菜单（刷新后保留选中状态）
const activeMenu = ref(route.path.replace('/', '') || 'record')

// 切换菜单
function handleMenuSelect(key) {
  activeMenu.value = key
  router.push(`/${key}`)
}

// 退出登录
function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}

// 如果用户没登录，直接踢回登录页
onMounted(() => {
  const token = localStorage.getItem('token')
  if (!token) router.push('/login')
})
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409eff;
}
</style>
