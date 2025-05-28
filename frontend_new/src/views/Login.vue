<template>
  <div class="login-page">
    <div class="login-card">
      <h2>用户登录</h2>
      <input v-model="username" placeholder="用户名" autocomplete="username" />
      <input v-model="password" type="password" placeholder="密码" autocomplete="current-password" />
      <button class="login-btn" @click="onLogin">登录</button>
      <button class="register-btn" @click="goRegister">免费注册</button>
      <p v-if="errorMsg" class="err-msg">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      username: '',
      password: '',
      errorMsg: ''
    }
  },
  // 假设loginApi是调用登录接口的函数

  methods: {
    async onLogin() {
      this.errorMsg = ''
      if (!this.username || !this.password) {
        this.errorMsg = '请输入用户名和密码'
        return
      }
      try {
        const res = await axios.post('/api/login', {
          username: this.username,
          password: this.password
        })
        // 后端建议返回：{ code: 200, msg: "成功", username: ..., role: ... }
        if (res.data.code === 200) {
          localStorage.setItem('user', JSON.stringify(res.data))
          localStorage.setItem('userId', res.data.id)  // 加这一句！
          // 按角色跳转
          if (res.data.role === 'admin') {
            this.$router.replace('/admin')
          } else {
            this.$router.replace('/')
          }
        } else {
          // 登录失败
          this.errorMsg = res.data.msg || '用户名或密码错误'
        }
      } catch (e) {
        this.errorMsg = (e.response?.data?.msg) || '服务器访问异常'
      }
    },
    goRegister() {
      this.$router.push('/register')
    }
  },
}
</script>

<style scoped>
/* ...你的样式保持不变... */
.login-page {
  min-height: 100vh;
  background: linear-gradient(130deg,#f4fcf9 22%, #c1e8e4 100%);
  display:flex;
  align-items: center;
  justify-content: center;
}
.login-card {
  padding:42px 34px;
  border-radius:14px;
  background:#fff;
  box-shadow:0 8px 48px #e0e0e0;
  width:330px;
  display:flex;
  flex-direction:column;
  align-items:stretch;
}
h2 { text-align:center; margin-bottom:32px; }
input {
  font-size:1.08em;
  padding:8px 12px;
  margin-bottom:17px;
  border-radius:6px;
  border:1px solid #e1e1e1;
  outline:none;
}
.login-btn, .register-btn {
  font-size:1em;
  padding:8px 0;
  margin-bottom:12px;
  border:none;
  border-radius:5px;
  transition: background .2s;
  cursor:pointer;
}
.login-btn { background:#42b983; color:#fff;}
.login-btn:hover { background: #268869;}
.register-btn { background:#ddd; color:#555; margin-bottom: 16px;}
.register-btn:hover { background: #c7e2da;}
.err-msg {
  color: #e35050;
  text-align: center;
  margin: 7px 0 0 0;
}
</style>