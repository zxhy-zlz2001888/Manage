<template>
  <div class="register-page">
    <div class="register-card">
      <h2>用户注册</h2>
      <input
          v-model="form.username"
          class="input"
          placeholder="用户名"
          autocomplete="off"
      />
      <input
          v-model="form.password"
          class="input"
          type="password"
          placeholder="密码"
          autocomplete="new-password"
      />
      <input
          v-model="form.confirmPassword"
          class="input"
          type="password"
          placeholder="确认密码"
          autocomplete="new-password"
      />
      <input
          v-model="form.phone"
          class="input"
          placeholder="电话"
          autocomplete="off"
      />
      <input
          v-model="form.email"
          class="input"
          type="email"
          placeholder="电子邮件"
          autocomplete="off"
      />

      <div class="group">
        <span>性别：</span>
        <label><input type="radio" value="男" v-model="form.gender" /> 男</label>
        <label><input type="radio" value="女" v-model="form.gender" /> 女</label>
      </div>

      <div class="group">
        <span>籍贯：</span>
        <select v-model="form.origin" class="input">
          <option value="">请选择</option>
          <option value="北京">北京</option>
          <option value="上海">上海</option>
          <option value="广东">广东</option>
          <option value="香港">香港</option>
          <option value="台湾">台湾</option>
          <option value="海外">海外</option>
        </select>
      </div>

      <div class="group">
        <span>爱好：</span>
        <label><input type="checkbox" value="唱歌" v-model="form.hobbies" /> 唱歌</label>
        <label><input type="checkbox" value="跳舞" v-model="form.hobbies" /> 跳舞</label>
        <label><input type="checkbox" value="编程" v-model="form.hobbies" /> 编程</label>
        <label><input type="checkbox" value="运动" v-model="form.hobbies" /> 运动</label>
      </div>

      <div class="group">
        <span>个人简介：</span>
        <textarea v-model="form.intro" rows="3" class="input"></textarea>
      </div>

      <button class="submit-btn" @click="onRegister">注册</button>
      <p v-if="msg" :class="['msg', msgColor==='green' ? 'success':'error']">{{ msg }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
const router = useRouter()
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',        // 新增
  email: '',        // 新增
  gender: '',
  origin: '',
  hobbies: [],
  intro: ''
})

const msg = ref('')
const msgColor = ref('red')

const clearMsg = () => { msg.value = '' }

const onRegister = async () => {
  clearMsg()
  if (!form.value.username || !form.value.password || !form.value.confirmPassword) {
    msg.value = '请填写完整账号和密码'
    msgColor.value = 'red'
    return
  }
  if (!form.value.phone) {
    msg.value = '请填写电话'
    msgColor.value = 'red'
    return
  }
  if (!form.value.email) {
    msg.value = '请填写电子邮件'
    msgColor.value = 'red'
    return
  }
  const emailReg = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
  if (!emailReg.test(form.value.email)) {
    msg.value = '请输入有效的电子邮件地址'
    msgColor.value = 'red'
    return
  }
  const phoneReg = /^1[3-9]\d{9}$|^\d{8,14}$/
  if (!phoneReg.test(form.value.phone)) {
    msg.value = '请输入有效的电话号码'
    msgColor.value = 'red'
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    msg.value = '两次输入的密码不一致'
    msgColor.value = 'red'
    return
  }
  if (!form.value.gender) {
    msg.value = '请选择性别'
    msgColor.value = 'red'
    return
  }
  if (!form.value.origin) {
    msg.value = '请选择籍贯'
    msgColor.value = 'red'
    return
  }
  try {
    let payload = {
      ...form.value,
      hobbies: form.value.hobbies.join(',')
    }
    const res = await axios.post('http://localhost:8080/api/register', payload)
    if (res.data.code === 0) {
      msg.value = res.data.msg
      msgColor.value = 'green'
      setTimeout(() => {
        router.push('/login')
      }, 800)
    } else {
      msg.value = res.data.msg
      msgColor.value = 'red'
    }
  } catch (e) {
    msg.value = '服务器连接失败'
    msgColor.value = 'red'
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #e6faf4 0%, #c0e6e0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.register-card {
  width: 360px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 6px 36px #e0e0e0;
  padding: 40px 30px 28px 30px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
h2 {
  text-align: center;
  color: #42b983;
  margin-bottom: 28px;
}
.input {
  padding: 8px 12px;
  border: 1px solid #e1e1e1;
  border-radius: 6px;
  font-size: 1em;
  margin-bottom: 14px;
  outline: none;
  background: #fafbfa;
  transition: border 0.2s;
}
.input:focus {
  border-color: #42b983;
  background: #f5fffa;
}
.group {
  margin-bottom: 14px;
  font-size: 1em;
  color: #444;
}
.group span {
  margin-right: 8px;
  font-weight: 500;
}
.group label {
  margin-right: 16px;
  font-weight: 400;
}
textarea.input {
  resize: vertical;
  min-height: 56px;
}
.submit-btn {
  margin-top: 8px;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 7px;
  padding: 9px 0;
  font-size: 1.08em;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
  letter-spacing: 2px;
}
.submit-btn:hover {
  background: #238c6d;
}
.msg {
  margin: 14px 0 0 0;
  font-size: 1em;
  text-align: center;
}
.msg.success {
  color: #14814b;
}
.msg.error {
  color: #e35050;
}
</style>