<template>
  <!-- 提示信息 -->
  <div v-if="message" class="msg-tip">{{ message }}</div>

  <!-- 管理员发布新活动/服务 -->
  <fieldset v-if="user.role === 'admin'" class="add-activity">
    <legend>发布新活动/服务</legend>
    <div class="form-row">
      <input v-model="newActivity.name" class="form-input" placeholder="活动名称" />
      <input v-model="newActivity.price" type="number" class="form-input" placeholder="价格" />
      <input v-model="newActivity.stock" type="number" class="form-input" placeholder="名额" />
      <input v-model="newActivity.description" class="form-input" placeholder="描述" />
      <input v-model="newActivity.startTime" type="datetime-local" class="form-input" placeholder="开始时间" />
      <input v-model="newActivity.endTime" type="datetime-local" class="form-input" placeholder="结束时间" />
    </div>
    <div class="form-row button-row">
      <button class="btn-primary" @click="createActivity" :disabled="submitting">
        {{ submitting ? "发布中..." : "发布" }}
      </button>
    </div>
  </fieldset>

  <div class="table-wrapper">
    <table class="event-table">
      <thead>
      <tr>
        <th>名称</th>
        <th>价格</th>
        <th>名额</th>
        <th>描述</th>
        <th>开始时间</th>
        <th>结束时间</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="a in activities" :key="a.id">
        <template v-if="editId !== a.id">
          <td>{{ a.name }}</td>
          <td>￥{{ a.price }}</td>
          <td>{{ a.stock }}</td>
          <td title="a.description">{{ a.description }}</td>
          <td>{{ formatDateTime(a.startTime) }}</td>
          <td>{{ formatDateTime(a.endTime) }}</td>
          <td>
            <template v-if="user.role === 'admin'">
              <button class="btn-table" @click="startEdit(a)">编辑</button>
              <button class="btn-table btn-danger" @click="deleteActivity(a.id)">删除</button>
            </template>
            <template v-else>
              <button class="btn-table btn-primary" @click="signUp(a.id)">报名</button>
            </template>
          </td>
        </template>
        <template v-else>
          <td><input class="table-input" v-model="editActivity.name" /></td>
          <td><input class="table-input" type="number" v-model="editActivity.price" /></td>
          <td><input class="table-input" type="number" v-model="editActivity.stock" /></td>
          <td><input class="table-input" v-model="editActivity.description" /></td>
          <td><input class="table-input" type="datetime-local" v-model="editActivity.startTime" /></td>
          <td><input class="table-input" type="datetime-local" v-model="editActivity.endTime" /></td>
          <td>
            <button class="btn-table btn-success" @click="saveEdit(a.id)">保存</button>
            <button class="btn-table" @click="cancelEdit">取消</button>
          </td>
        </template>
      </tr>
      <tr v-if="activities.length===0">
        <td colspan="7" style="text-align: center;color:#777;">暂无活动</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const activities = ref([])
const submitting = ref(false)
const newActivity = ref({
  name: '',
  price: 0,
  stock: 0,
  description: '',
  startTime: '',
  endTime: ''
})
const editActivity = ref({
  name: '',
  price: 0,
  stock: 0,
  description: '',
  startTime: '',
  endTime: ''
})
defineProps({
  isAdmin: Boolean,

});
const editId = ref(null)
const message = ref('')

const user = JSON.parse(localStorage.getItem('user') || '{}')

async function fetchActivities() {
  const res = await axios.get('/api/activities', { headers: { 'X-Role': user.role } })
  activities.value = res.data
}
onMounted(fetchActivities)

async function createActivity() {
  if (submitting.value) return
  if (!newActivity.value.name) {
    message.value = '请填写活动名称'
    return
  }
  submitting.value = true
  try {
    const res = await axios.post('/api/activities', newActivity.value, { headers: { 'X-Role': user.role } })
    activities.value.push(res.data)
    Object.assign(newActivity.value, { name: '', price: 0, stock: 0, description: '', startTime: '', endTime: '' })
    message.value = "添加成功"
  } catch { message.value = "添加失败" }
  submitting.value = false
}

function startEdit(a) {
  editId.value = a.id
  editActivity.value = { ...a }
}
function cancelEdit() {
  editId.value = null
}
async function saveEdit(id) {
  if (submitting.value) return
  submitting.value = true
  try {
    const res = await axios.put(`/api/activities/${id}`, editActivity.value, { headers: { 'X-Role': user.role } })
    const idx = activities.value.findIndex(act => act.id === id)
    if (idx !== -1) activities.value[idx] = res.data
    editId.value = null
    message.value = "修改成功"
  } catch { message.value = "修改失败" }
  submitting.value = false
}

async function deleteActivity(id) {
  if (submitting.value) return
  if (!window.confirm("确认删除该活动？")) return
  submitting.value = true
  try {
    await axios.delete(`/api/activities/${id}`, { headers: { 'X-Role': user.role } })
    activities.value = activities.value.filter(a => a.id !== id)
    message.value = "已删除"
  } catch { message.value = "删除失败" }
  submitting.value = false
}

// 普通用户报名参加活动
async function signUp(activityId) {
  try {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) {
      alert("请先登录！");
      return;
    }
    const res = await axios.post('/api/appointments', {
      userId: user.id,
      activityId: activityId
    });
    if (res.data && (res.data.code === 200 || res.status === 200)) {
      alert("待审核！");
    } else {
      alert(res.data.msg || "报名失败！");
    }
  } catch (e) {
    alert("报名失败，可能已报过或网络异常。");
  }
}

// 格式化时间(去掉T，美观)
function formatDateTime(val) {
  if (!val) return ''
  return val.replace('T', ' ').slice(0, 16)
}
</script>
<style scoped>
.msg-tip {
  background: #eaf6ff;
  border-left: 4px solid #42a5f5;
  padding: 8px 16px;
  color: #2173b4;
  font-size: 1.03em;
  margin-bottom: 14px;
  border-radius: 5px;
  max-width: 890px;
  margin-left: auto;
  margin-right: auto;
  letter-spacing: 1px;
}

.add-activity {
  border: 1.3px solid #d0e2fc;
  border-radius: 13px;
  background: #f8fbff;
  margin-bottom: 26px;
  box-shadow: 0 2px 14px #a1bfff09;
  padding: 18px 22px 6px 22px;
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
}

.add-activity legend {
  padding: 0 14px;
  color: #376bc6;
  font-size: 1.15em;
  font-weight: 700;
  letter-spacing: 1px;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: left;
  margin-bottom: 16px;
}
.form-input {
  flex: 1 1 120px;
  min-width: 133px;
  padding: 8px 12px;
  border-radius: 7px;
  border: 1px solid #bedaf5;
  outline: none;
  font-size: 1.05em;
  background: #f7fbfe;
  transition: border 0.18s;
}
.form-input:focus {
  border: 1.3px solid #4ca7fd;
  background: #fff;
}

.button-row {
  margin-top: 2px;
  justify-content: right;
  gap: 0;
}

.btn-primary,
.btn-success,
.btn-danger,
.btn-table {
  display: inline-block;
  border-radius: 7px;
  border: none;
  padding: 0.43em 1.4em;
  font-size: 1.04em;
  font-weight: 500;
  margin-right: 10px;
  background: #467cf4;
  color: #fff;
  cursor: pointer;
  transition: background .14s, color .15s, box-shadow 0.16s;
  box-shadow: 0 0.5px 4px #5c9cf422;
  outline: none;
}
.btn-primary[disabled] {opacity: 0.6; cursor: not-allowed;}
.btn-success {background: #03b77e;}
.btn-danger {background: #e84e4a;}
.btn-table {
  padding: 0.26em 1em;
  font-size: .98em;
  background: #f2f5fc;
  color: #376bc6;
  border: 1px solid #e5ebf7;
  box-shadow: none;
  margin-right: 4px;
}
.btn-table.btn-danger {background: #ffeaea; color:#c71818; border: 1px solid #fedede;}
.btn-table.btn-success {background:#e7faef; color:#06a75d; border: 1px solid #c3f9da;}
.btn-table.btn-primary {background:#e8f0fd; color:#2456a7; border: 1px solid #bedaf5;}
.btn-table:hover {background: #e3ebfb;}
.btn-table.btn-danger:hover {background: #ffdbdc;}
.btn-table.btn-success:hover {background:#c5f5da;}
.btn-table.btn-primary:hover {background:#d4e6fa;}

.table-wrapper {
  overflow-x: auto;
  max-width: 100vw;
  margin: 0 auto;
}
.event-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 12px;
  border-radius: 12px;
  box-shadow: 0 3px 20px #5677c928;
  overflow: hidden;
  background: #fcfdff;
}
.event-table th, .event-table td {
  padding: 13px 8px;
  border-bottom: 1px solid #e5edfa;
  text-align: center;
}
.event-table th {
  background: #eaf1fa;
  color: #2155bd;
  font-weight: 700;
  letter-spacing: 1px;
}
.event-table tr:last-child td {border-bottom: none;}
.event-table tr:hover {background: #f4f8fd;}

.table-input {
  width: 98%;
  min-width: 60px;
  padding: 5px 8px;
  border-radius: 6px;
  border: 1px solid #cdd9ef;
  background: #f7fafd;
  font-size: .98em;
}

/* 响应式优化 */
@media (max-width: 850px) {
  .add-activity, .table-wrapper {padding-left: 0; padding-right: 0;}
  .event-table th, .event-table td { padding: 10px 4px; font-size: .98em;}
  .form-row .form-input {flex: 1 1 90px;}
  .msg-tip {max-width: 99vw;}
}
@media (max-width: 500px) {
  .form-row {flex-direction: column;}
  .event-table th, .event-table td { font-size: .92em;}
  .btn-table, .btn-primary, .btn-danger { font-size:.92em; padding: 0.3em 0.7em;}
}
</style>
