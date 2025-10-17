import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/home', component: Home }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 判断是否已登录
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 如果没有 token 并且访问的不是登录页 → 强制跳回登录
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
