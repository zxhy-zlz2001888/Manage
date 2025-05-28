<template>
  <div class="admin-panel">

    <!-- 消息提示 -->
    <transition name="fade">
      <div v-if="message" class="message">{{ message }}</div>
    </transition>

    <!-- 新增用户表单 -->
    <fieldset class="add-user">
      <legend>添加新用户</legend>
      <div class="form-row">
        <input v-model.trim="newUser.username" placeholder="用户名" />
        <input v-model.trim="newUser.password" placeholder="密码" type="password" />
        <select v-model="newUser.gender">
          <option value="">性别</option>
          <option v-for="g in genders" :key="g" :value="g">{{ g }}</option>
        </select>
        <select v-model="newUser.origin">
          <option value="">籍贯</option>
          <option v-for="c in cities" :key="c" :value="c">{{ c }}</option>
        </select>
      </div>
      <div class="form-row hobbies">
        <span>爱好：</span>
        <label v-for="h in hobbiesOptions" :key="h">
          <input type="checkbox" :value="h" v-model="newUser.hobbiesArray" />{{ h }}
        </label>
      </div>
      <div class="form-row">
        <textarea v-model="newUser.intro" placeholder="简介"></textarea>
      </div>
      <div class="form-row button-row">
        <button @click="createUser" :disabled="submitting">
          {{ submitting ? "添加中..." : "添加用户" }}
        </button>
      </div>
    </fieldset>

    <!-- 用户列表 -->
    <table class="user-table">
      <thead>
      <tr>
        <th>用户名</th>
        <th>性别</th>
        <th>籍贯</th>
        <th>爱好</th>
        <th>简介</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="u in users" :key="u.id">
        <template v-if="editId !== u.id">
          <td>{{ u.username }}</td>
          <td>{{ u.gender }}</td>
          <td>{{ u.origin }}</td>
          <td>{{ u.hobbiesArray.join(", ") }}</td>
          <td>{{ u.intro }}</td>
          <td>
            <button @click="startEdit(u)">编辑</button>
            <button @click="deleteUser(u.id)" :disabled="submitting">删除</button>
          </td>
        </template>
        <template v-else>
          <td><input v-model="editUser.username" /></td>
          <td>
            <select v-model="editUser.gender">
              <option value="">性别</option>
              <option v-for="g in genders" :key="g" :value="g">{{ g }}</option>
            </select>
          </td>
          <td>
            <select v-model="editUser.origin">
              <option value="">籍贯</option>
              <option v-for="c in cities" :key="c" :value="c">{{ c }}</option>
            </select>
          </td>
          <td>
            <div class="hobbies-edit">
              <label v-for="h in hobbiesOptions" :key="h">
                <input type="checkbox" :value="h" v-model="editUser.hobbiesArray" />{{ h }}
              </label>
            </div>
          </td>
          <td>
            <input v-model="editUser.intro" />
          </td>
          <td>
            <button @click="saveEdit(u.id)" :disabled="submitting">保存</button>
            <button @click="cancelEdit">取消</button>
          </td>
        </template>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const genders = ['男', '女', '其他']
const cities = ['北京', '上海', '广州', '深圳', '杭州', '成都','重庆','武汉','西安','南京','苏州','香港','台北']
const hobbiesOptions = ['音乐', '运动', '旅行', '读书', '摄影', '代码', '美食', '游戏', '电影', '手工']

const users = ref([])
const message = ref("")
const submitting = ref(false)

// 获取当前登录用户(防止localStorage为空)
const user = JSON.parse(localStorage.getItem('user') || '{}')

// 跳转辅助
function jumpToLogin() { window.location.href = "/login" }

// 判断如果user.role不存在，即不是管理员，提醒并跳转
if (!user.role || user.role !== 'admin') {
  showMsg("无权访问该页面，请用管理员账号登录！")
  setTimeout(jumpToLogin, 1600)
}

// 提示函数 (自动消失)
function showMsg(msg, duration=2000) {
  message.value = msg
  if (duration>0)
    setTimeout(() => message.value = "", duration)
}

// 新用户表单数据
const newUser = ref({
  username: '',
  password: '',
  gender: '',
  origin: '',
  hobbies: '',
  hobbiesArray: [],
  intro: ''
})

async function fetchUsers() {
  try {
    const res = await axios.get('/api/users', {
      headers: { 'X-Role': user.role }
    })
    users.value = Array.isArray(res.data)
        ? res.data.map(u => ({
          ...u,
          hobbiesArray: u.hobbies ? u.hobbies.split(',') : [],
        }))
        : []
  } catch (e) {
    if (e.response && e.response.status === 403) {
      showMsg("无权访问用户列表，请用管理员账号登录")
      setTimeout(jumpToLogin, 800)
    } else {
      showMsg("无法获取用户列表")
    }
    users.value = []
  }
}
onMounted(fetchUsers)

async function createUser() {
  if(submitting.value) return
  if (!newUser.value.username || !newUser.value.password || !newUser.value.gender || !newUser.value.origin) {
    showMsg("请填写完整信息", 1800)
    return
  }
  submitting.value = true
  try {
    newUser.value.hobbies = newUser.value.hobbiesArray.join(',')
    const payload = { ...newUser.value }
    const res = await axios.post('/api/users', payload, {
      headers: { 'X-Role': user.role }
    })
    users.value.push({
      ...res.data,
      hobbiesArray: res.data.hobbies ? res.data.hobbies.split(',') : [],
    })
    showMsg("添加成功！", 1500)
    newUser.value = { username: '', password: '', gender: '', origin: '', hobbies: '', hobbiesArray: [], intro: '' }
  } catch(e){
    if (e.response && e.response.status === 403) {
      showMsg("无权添加用户，未认证管理员", 2000)
      setTimeout(jumpToLogin, 1300)
    } else {
      showMsg("添加失败", 1800)
    }
  }
  submitting.value = false
}

const editId = ref(null)
const editUser = ref({
  username: '', password: '', gender: '', origin: '', hobbies: '', hobbiesArray: [], intro: ''
})

function startEdit(user) {
  editId.value = user.id
  editUser.value = { ...user, password: '', hobbiesArray: user.hobbies ? user.hobbies.split(',') : [] }
}
async function saveEdit(id) {
  if(submitting.value) return
  if (!editUser.value.username || !editUser.value.gender || !editUser.value.origin) {
    showMsg("请完整编辑信息", 1600)
    return
  }
  submitting.value = true
  editUser.value.hobbies = editUser.value.hobbiesArray.join(',')
  let payload = { ...editUser.value }
  if (!payload.password) delete payload.password
  try {
    const res = await axios.put(`/api/users/${id}`, payload, {
      headers: { 'X-Role': user.role }
    })
    res.data.hobbiesArray = res.data.hobbies ? res.data.hobbies.split(',') : []
    const idx = users.value.findIndex(u => u.id === id)
    if (idx !== -1) users.value[idx] = res.data
    editId.value = null
    showMsg("修改成功", 1400)
  } catch (e) {
    if (e.response && e.response.status === 403) {
      showMsg("无权修改用户，未认证管理员", 1800)
      setTimeout(jumpToLogin, 1200)
    } else {
      showMsg("修改失败", 1600)
    }
  }
  submitting.value = false
}
function cancelEdit() { editId.value = null }

async function deleteUser(id) {
  if(submitting.value) return
  if(!window.confirm("确认删除该用户？")) return
  submitting.value = true
  try {
    await axios.delete(`/api/users/${id}`, {
      headers: { 'X-Role': user.role }
    })
    users.value = users.value.filter(u => u.id !== id)
    showMsg("已删除", 1300)
  } catch (e) {
    if (e.response && e.response.status === 403) {
      showMsg("无权删除用户，未认证管理员", 1500)
      setTimeout(jumpToLogin, 1200)
    } else {
      showMsg("删除失败", 1300)
    }
  }
  submitting.value = false
}
</script>

<style scoped>
.admin-panel {
  max-width: 1000px;
  margin: 36px auto;
  background: #fafcff;
  border-radius: 14px;
  box-shadow: 0 6px 32px #78b0ee12;
  padding: 38px 28px 28px 28px;
}
h2 { color: #3567bc; text-align: center; margin: 0 0 28px 0; }
.message {
  background: #def7f8;
  color: #26a69b;
  border-radius: 4px;
  padding: 7px 18px;
  margin-bottom: 14px;
  text-align: center;
  font-size: 1em;
  transition: all .2s;
}
.fade-enter-active,.fade-leave-active { transition: opacity .5s; }
.fade-enter-from,.fade-leave-to { opacity: 0; }
fieldset.add-user {
  border:1.5px solid #e9e9e9;
  border-radius: 9px;
  margin-bottom: 29px;
  padding: 20px 18px 14px;
  background: #f7fafc;
}
.add-user .form-row {
  margin-bottom:14px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.add-user .form-row textarea {
  width: 360px;
  min-height: 32px;
  resize: vertical;
}
.add-user .hobbies label { margin-right: 9px; }
.add-user .button-row button {
  font-size: 1em;
  padding: 8px 32px;
  border: none;
  background: #3877e9;
  color: #fff;
  border-radius: 7px;
  margin-top: 3px;
  box-shadow: 0 2px 10px #20448810;
  cursor: pointer;
  transition: background .22s;
}
.add-user .button-row button:disabled { background: #aaa; cursor: not-allowed;}
.user-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  font-size: 1em;
  margin-top: 20px;
  border-radius:11px;
  overflow: hidden;
  box-shadow: 0 3px 16px #abc2ee18;
}
.user-table th, .user-table td {
  padding: 12px 8px;
  border: 1px solid #ededed;
  text-align: center;
}
.user-table th { background: #f1f7fd; color: #437bc6;}
.user-table tr:nth-child(even) { background: #f7fafd;}
.user-table input,
.user-table select {
  width:90%;
  padding:3px 7px;
  border-radius: 4px;
  border:1px solid #dde3ee;
  background: #fafcff;
}
.hobbies-edit label { margin-right: 7px; }
button {
  margin: 0 3px; padding: 4px 13px; border-radius: 4px; border: none; background: #2a76e7; color: #fff; cursor: pointer;
  transition: background .16s;
}
button:disabled { opacity: 0.6; cursor: default;}
button:hover:not(:disabled) { background: #1b54b2;}
</style>