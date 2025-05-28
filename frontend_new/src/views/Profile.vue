<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const user = ref({})
const loading = ref(true)
const errorMsg = ref("")

onMounted(async () => {
  loading.value = true
  errorMsg.value = ""
  const userId = localStorage.getItem('userId');
  if (!userId) {
    errorMsg.value = '未登录，请先登录！'
    loading.value = false
    return;
  }
  try {
    const res = await axios.get('http://localhost:8080/api/profile', {
      params: { userId }
    });
    if (res.data.hobbies && typeof res.data.hobbies === 'string') {
      res.data.hobbies = res.data.hobbies.split(/[，,]/).filter(s=>s.trim());
    }
    user.value = res.data
  } catch (err) {
    errorMsg.value = '获取个人信息失败，请稍后重试！'
    console.error(err)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="profile-bg">
    <div class="profile-page ui-card">
      <h2 class="title">
        <span class="avatar">👤</span>
        个人信息
      </h2>

      <div v-if="loading">
        <p class="loading">正在加载...</p>
      </div>

      <div v-else>
        <div v-if="errorMsg">
          <p class="error">{{ errorMsg }}</p>
        </div>
        <div v-else-if="user.username">
          <ul class="info-list">
            <li class="info-row">
              <span class="icon">🆔</span>
              <b>用户名：</b><span class="v">{{ user.username }}</span>
            </li>
            <li class="info-row">
              <span class="icon">⚧️</span>
              <b>性别：</b>
              <span class="v" :class="{ empty: !user.gender }">{{ user.gender || '未填写' }}</span>
            </li>
            <li class="info-row">
              <span class="icon">🏠</span>
              <b>籍贯：</b>
              <span class="v" :class="{ empty: !user.origin }">{{ user.origin || '未填写' }}</span>
            </li>
            <li class="info-row">
              <span class="icon">🎉</span>
              <b>爱好：</b>
              <span class="v" v-if="user.hobbies && user.hobbies.length">{{ user.hobbies.join('，') }}</span>
              <span class="v empty" v-else>无</span>
            </li>
            <li class="info-row">
              <span class="icon">💬</span>
              <b>简介：</b>
              <span class="v" :class="{ empty: !user.intro }">{{ user.intro || '暂无' }}</span>
            </li>
            <li class="info-row">
              <span class="icon">📱</span>
              <b>手机号：</b>
              <span class="v" :class="{ empty: !user.phone }">{{ user.phone || '未填写' }}</span>
            </li>
            <li class="info-row">
              <span class="icon">✉️</span>
              <b>联系邮箱：</b>
              <span class="v" :class="{ empty: !user.email }">{{ user.email || '未填写' }}</span>
            </li>
          </ul>
        </div>
        <div v-else>
          <p class="error">未获取到用户信息，请先登录。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #e0fcff 0%, #f5f7fa 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-page {
  min-width: 350px;
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 8px 32px #ddeefe88;
  padding: 38px 38px 28px 38px;
  margin: 40px 0;
}

.title {
  text-align: left;
  font-weight: bold;
  letter-spacing: 1px;
  margin-bottom: 28px;
  font-size: 1.47em;
  display: flex;
  align-items: center;
}
.avatar {
  font-size: 1.45em;
  margin-right: 9px;
}

.info-list {
  margin-top: 16px;
  list-style: none;
  padding: 0;
}
.info-row {
  display: flex;
  align-items: center;
  font-size: 1.08em;
  padding: 8px 0;
  border-bottom: 1px solid #f2f2f2;
}
.icon {
  width: 2em;
  margin-right: 8px;
  font-size: 1.06em;
  opacity: 0.7;
  flex-shrink: 0;
}
.info-row b {
  color: #222;
  font-weight: 500;
  min-width: 67px;
}
.v {
  color: #444;
  margin-left: 7px;
  font-size: 1em;
}
.v.empty {
  color: #aaa;
  font-style: italic;
}
.error {
  color: #ff4d4f;
  background: #fff1f0;
  border-radius: 6px;
  padding: 13px;
  font-weight: 500;
  font-size: 1.05em;
  text-align: center;
  margin: 15px auto;
}
.loading {
  color: #73d13d;
  font-size: 1.1em;
  font-weight: 500;
  letter-spacing: 1px;
  text-align: center;
  margin: 32px 0 12px 0;
}
</style>