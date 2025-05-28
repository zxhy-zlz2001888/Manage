<template>
  <div class="my-appointments">
    <h2>我的预约</h2>
    <div class="table-wrap">
      <table v-if="appointments.length > 0" class="event-table">
        <thead>
        <tr>
          <th>用户名</th>
          <th>名称</th>
          <th>价格</th>
          <th>名额</th>
          <th>描述</th>
          <th>开始时间</th>
          <th>结束时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="a in appointments" :key="a.id">
          <td>{{ a.username }}</td>
          <td>{{ a.activityName }}</td>
          <td>{{ a.price }}</td>
          <td>{{ a.stock }}</td>
          <td>{{ a.description }}</td>
          <td>{{ formatDateTime(a.startTime) }}</td>
          <td>{{ formatDateTime(a.endTime) }}</td>
          <td>
            <span
                class="badge"
                :class="{
                approved: a.status === '已通过',
                pending: a.status === '待审核',
                rejected: a.status === '已拒绝'
              }"
            >
              {{ a.status || '已报名' }}
            </span>
          </td>
          <td>
            <button class="btn-cancel" @click="cancelAppointment(a.id)">取消预约</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="no-data" v-else>
        暂无预约记录
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const appointments = ref([])

const user = JSON.parse(localStorage.getItem('user') || '{}')

function formatDateTime(val) {
  if (!val) return ''
  return val.replace('T', ' ').slice(0, 16)
}

async function fetchAppointments() {
  const res = await axios.get('/api/appointments/my', {
    headers: { 'X-User-Id': user.id }
  })
  appointments.value = res.data
}

async function cancelAppointment(id) {
  if (!window.confirm("确定要取消该预约吗？")) return
  await axios.delete(`/api/appointments/${id}`)
  appointments.value = appointments.value.filter(item => item.id !== id)
}

onMounted(fetchAppointments)
</script>

<style scoped>
.my-appointments {
  min-height: 80vh;
  padding: 40px 0 20px 0;
  background: linear-gradient(115deg, #edf6fc 0%, #e7f9f5 80%);
}
h2 {
  text-align: center;
  color: #3a91df;
  font-size: 2rem;
  margin-bottom: 30px;
  letter-spacing: 2px;
}
.table-wrap {
  max-width: 1150px;
  background: #fff;
  margin: 0 auto;
  border-radius: 15px;
  box-shadow: 0 8px 36px #e5ebfc88;
  padding: 20px 16px 40px 16px;
  min-height: 240px;
}
.event-table {
  width: 100%;
  border-collapse: collapse;
  background: #fafcff;
  border-radius: 10px;
  box-shadow: 0 2px 10px #d5e6fa22;
  overflow: hidden;
  margin: 0 auto;
  font-size: 1.04em;
}
.event-table th {
  background: linear-gradient(90deg, #daf1fd 0%, #e8fff9 60%);
  color: #2279d3;
  font-weight: 600;
  padding: 13px 0;
  border: none;
  font-size: 1em;
  letter-spacing: 1px;
}
.event-table tr {
  transition: background 0.13s;
}
.event-table tbody tr:nth-child(odd) {
  background: #f7fbff;
}
.event-table tbody tr:hover {
  background: #e8f1fe;
}
.event-table td {
  padding: 13px 7px;
  color: #2a6ea6;
  font-size: 0.97em;
  border-bottom: 1px solid #eaeaea;
  text-align: center;
  word-break: break-word;
}
.badge {
  display: inline-block;
  padding: 3px 13px;
  border-radius: 14px;
  font-weight: bold;
  font-size: 0.97em;
  border: 1.5px solid #b4defb;
  background: #eefdff;
}
.badge.approved {
  background: #e8faec;
  color: #15bd52;
  border-color: #9beab1;
}
.badge.pending {
  background: #fffbe7;
  color: #ec9e00;
  border-color: #ffe263;
}
.badge.rejected {
  background: #fff1f1;
  color: #e51c30;
  border-color: #f7b2be;
}
.btn-cancel {
  background: linear-gradient(90deg, #51d5ef 0%, #89f7ce 100%);
  color: #267182;
  font-weight: bold;
  border: none;
  border-radius: 7px;
  padding: 5px 20px;
  font-size: 1em;
  cursor: pointer;
  box-shadow: 0 2px 10px #cef6ff45;
  transition: background 0.15s, color 0.15s;
}
.btn-cancel:hover {
  background: #37b6f0;
  color: #fff;
}
.no-data {
  color: #bbb;
  font-size: 1.14em;
  padding: 80px 0;
  text-align: center;
}
@media (max-width: 870px) {
  .table-wrap { padding: 10px 1vw 22px 1vw; }
  .event-table td, .event-table th { padding: 8px 3px; font-size: 0.96em; }
}
@media (max-width:500px) {
  .event-table { font-size: 0.96em; }
  .table-wrap { padding: 8px 0 16px 0; }
  h2 { font-size: 1.2em; }
}
</style>