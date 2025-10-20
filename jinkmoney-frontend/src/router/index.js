import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import Record from '../views/Record.vue'
// import Stats from '../views/Stats.vue'

// 路由表
const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/home',
    component: Home,
    children: [
      { path: '/record', component: Record },
      // { path: '/stats', component: Stats } // 
    ]
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：判断是否登录
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 未登录且不是在登录页 → 跳转登录
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
