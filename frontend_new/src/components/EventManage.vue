<template>
  <fieldset class="add-activity">
    <legend>发布新活动/服务</legend>
    <div class="form-row">
      <input v-model="newActivity.name" placeholder="活动名称" />
      <input v-model="newActivity.price" type="number" placeholder="价格" />
      <input v-model="newActivity.stock" type="number" placeholder="名额" />
      <input v-model="newActivity.description" placeholder="描述" />
      <input v-model="newActivity.startTime" type="datetime-local" placeholder="开始时间" />
      <input v-model="newActivity.endTime" type="datetime-local" placeholder="结束时间" />
    </div>
    <div class="form-row button-row">
      <button @click="createActivity" :disabled="submitting">
        {{ submitting ? "发布中..." : "发布" }}
      </button>
    </div>
    <div class="feedback" v-if="message">{{ message }}</div>
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
          <td>{{ a.price }}</td>
          <td>{{ a.stock }}</td>
          <td>{{ a.description }}</td>
          <td>{{ formatDateTime(a.startTime) }}</td>
          <td>{{ formatDateTime(a.endTime) }}</td>
          <td>
            <button class="op-btn edit" @click="startEdit(a)">编辑</button>
            <button class="op-btn danger" @click="deleteActivity(a.id)">删除</button>
          </td>
        </template>
        <template v-else>
          <td><input v-model="editActivity.name" /></td>
          <td><input type="number" v-model="editActivity.price" /></td>
          <td><input type="number" v-model="editActivity.stock" /></td>
          <td><input v-model="editActivity.description" /></td>
          <td><input type="datetime-local" v-model="editActivity.startTime" /></td>
          <td><input type="datetime-local" v-model="editActivity.endTime" /></td>
          <td>
            <button class="op-btn save" @click="saveEdit(a.id)">保存</button>
            <button class="op-btn cancel" @click="cancelEdit">取消</button>
          </td>
        </template>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
// --- JS部分保持不变 ---
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

function formatDateTime(val) {
  if (!val) return ''
  return val.replace('T',' ').slice(0,16)
}
</script>

<style scoped>
.add-activity {
  background: linear-gradient(120deg,#eaf6fb 0%, #f3faf8 100%);
  border-radius: 16px;
  box-shadow: 0 6px 32px #dbeefa80, 0 1.5px 3.5px rgba(50,100,140,0.12);
  padding: 32px 36px 20px 36px;
  max-width: 720px;
  margin: 34px auto 34px auto;
  border: none;
  position: relative;
  transition: box-shadow .22s;
}

.add-activity legend {
  font-size: 1.3em;
  color: #1199ff;
  font-weight: 700;
  letter-spacing: 3px;
  padding: 0 20px;
  margin-left: 24px;
  background: linear-gradient(90deg,#fafdff 70%,#e4f0ff 100%);
  border-radius: 12px;
  border-bottom: 2.5px solid #b8daff44;
  line-height: 1.7;
  margin-bottom: 16px;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 17px;
  margin-bottom: 22px;
  justify-content: space-between;
}
.form-row input {
  background: #fbfdff;
  border: 1.2px solid #c2e5fa;
  border-radius: 8px;
  font-size: 1.03em;
  padding: 8px 14px;
  min-width: 120px;
  width: 173px;
  outline: none;
  box-shadow: 0 1px 4px #e2f5fc66;
  transition: border 0.18s, box-shadow 0.21s;
}
.form-row input:focus {
  border-color: #45a8fa;
  background: #f6fdfe;
  box-shadow: 0 0 7px #d5f2feaf;
}
.button-row {
  justify-content: flex-end;
  margin-top: 8px;
}
.add-activity button {
  background: linear-gradient(90deg,#14c8ff 0%, #22cf8c 100%);
  color: #fff;
  border: none;
  border-radius: 9px;
  font-size: 1.13em;
  padding: 9px 40px;
  margin-top: 3px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 2px 15px #01c6fd13;
  transition: background 0.16s, box-shadow 0.18s, filter .19s;
  letter-spacing: 3px;
}
.add-activity button:active {
  filter: brightness(.95);
}
.add-activity button:disabled {
  background: #b2c2d9;
  cursor: not-allowed;
  filter: grayscale(0.11);
}
.add-activity .feedback {
  margin-top: 11px;
  font-size: 1.01em;
  text-align: right;
  min-height: 23px;
  color: #27ac9e;
}
.add-activity .feedback:empty { min-height: 0; }

.table-wrapper {
  max-width: 1050px;
  margin: 0 auto 32px;
  background: linear-gradient(105deg,#edf9fb 60%,#f7fcfe 100%);
  box-shadow: 0 5px 26px #e7f4fc25;
  border-radius: 13px;
  padding: 14px 2.5vw 28px 2.5vw;
  overflow-x: auto;
}

.event-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 5px;
  font-size: 1.08em;
  background: none;
  border-radius: 13px;
  box-shadow: none;
  margin: 0 auto;
}
.event-table thead tr {
  background: linear-gradient(90deg,#def1fe 0%, #e2fbea 100%);
  border-radius: 16px 16px 0 0;
}
.event-table th, .event-table td {
  padding: 12px 9px;
  border: none;
  text-align: center;
}
.event-table th {
  color: #188fc7;
  font-size: 1.11em;
  font-weight: bold;
  letter-spacing: 1px;
}
.event-table tbody tr {
  background: #fff;
  border-radius: 9px;
  box-shadow: 0 2px 13px #e0eaf31b;
  transition: background 0.17s, box-shadow 0.18s;
}
.event-table tbody tr:hover {
  background: #daefff4d;
  box-shadow: 0 2px 18px #b3e5f988;
}
.event-table tbody tr:nth-child(even) {
  background: #f9fdff;
}
.event-table input[type="text"],
.event-table input[type="number"],
.event-table input[type="datetime-local"] {
  width: 98%;
  background: #f6fcfa;
  padding: 6px 8px;
  border: 1px solid #deeff5;
  border-radius: 5px;
  outline: none;
}
.event-table input:focus {
  border-color: #1fd0ab;
  background: #f3fff6;
}
.op-btn {
  border: none;
  border-radius: 7px;
  padding: 7px 18px;
  margin: 0 3px;
  font-weight: bold;
  font-size: 1em;
  background: #e7f6fe;
  color: #1886e4;
  cursor: pointer;
  box-shadow: 0 1px 4px #b6f6f933;
  transition: background .16s, color .16s, box-shadow .15s;
}
.op-btn.edit {
  background: linear-gradient(90deg,#59b7ff 0%, #bcf3e4 100%);
  color: #1666c6;
}
.op-btn.danger {
  background: linear-gradient(90deg,#ff8282 0%, #ffeaea 100%);
  color: #c61b1b;
}
.op-btn.save {
  background: linear-gradient(90deg,#24d2a9 0%, #addfdc 100%);
  color: #116157;
}
.op-btn.cancel {
  background: #eaedf7;
  color: #aaa;
}
.op-btn:disabled {
  opacity: 0.66;
  cursor: not-allowed;
}
@media (max-width: 900px) {
  .table-wrapper, .add-activity { max-width: 96vw; padding: 11px 2vw;}
  .add-activity { padding: 18px 2vw 9px 2vw;}
  .event-table th, .event-table td { padding: 7px 2px; font-size: 0.98em;}
  .form-row { flex-direction: column; gap: 8px; }
}
</style>