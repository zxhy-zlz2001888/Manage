<template>
  <div id="app">
    <div v-if="route.path !== '/login'">
      <!-- 退出按钮 -->
      <button @click="logout" style="position:fixed;top:10px;right:10px;z-index:1000;">
        退出
      </button>

      <!-- 只有非管理员才显示主导航 -->
      <nav class="main-nav" v-if="user.role !== 'admin'">
        <router-link to="/about">关于我们</router-link>
        <router-link to="/notices">公告</router-link>
        <router-link to="/appointments">我的预约</router-link>
        <router-link to="/profile">个人信息</router-link>
        <router-link to="/activities">预约项目</router-link>
      </nav>

      <!-- 管理员独占入口 -->
      <nav class="main-nav" v-if="user.role === 'admin'">
        <router-link to="/admin" class="admin-link">管理员</router-link>
      </nav>
    </div>
    <router-view/>
  </div>
</template>
<script setup>
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'


// 获取路由
const route = useRoute()
const router = useRouter()

// 退出登录
function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

// 取用户信息
const user = JSON.parse(localStorage.getItem('user') || '{}')
// const user = computed(() => {
//   try {
//     return JSON.parse(localStorage.getItem('user')) || {}
//   } catch {
//     return {}
//   }
// })
</script>
<style scoped>
.admin-link.disabled {
  pointer-events: none;    /* 禁止点击 */
  background: #ececec;
  color: #bdbdbd !important;
  cursor: not-allowed;
}
.admin-link {
  background: #EBEBEB;
  border-radius: 12px;
  padding: 12px 36px;
  font-size: 32px;
  color: #555;
  margin-left: 20px;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-block;
}
.main-nav a.disabled {
  color:#aaa; cursor:not-allowed; pointer-events:auto;
}
.main-nav {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 28px;
  background: linear-gradient(90deg, #2266ee 0%, #55bbff 100%);
  padding: 18px 0;
  box-shadow: 0 4px 14px rgba(50, 50, 100, 0.08);
  border-radius: 12px;
  margin: 24px auto;
  max-width: 780px;
  font-family: 'Segoe UI', 'PingFang SC', sans-serif;
}

.main-nav a {
  color: #fff;
  text-decoration: none;
  font-weight: 500;
  font-size: 17px;
  padding: 9px 24px;
  border-radius: 6px;
  transition: background 0.2s, color 0.2s;
}

.main-nav a.router-link-active,
.main-nav a:hover {
  background: rgba(255,255,255,0.17);
  color: #ffd43b;
}

@media (max-width: 600px) {
  .main-nav {
    flex-direction: column;
    gap: 10px;
    border-radius: 8px;
    padding: 10px 0;
  }
  .main-nav a {
    padding: 8px 16px;
    font-size: 15px;
  }
}
</style>