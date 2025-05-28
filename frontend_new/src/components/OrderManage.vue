<template>
  <div class="admin-main">
    <div class="search-box">
      <input v-model="filters.username" placeholder="用户名" />
      <input type="date" v-model="filters.date" />
      <button @click="fetchOrders">筛选</button>
    </div>
    <div class="table-wrap">
      <table class="order-table">
        <thead>
        <tr>
          <th>预约用户</th>
          <th>预约活动</th>
          <th>日期</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.username }}</td>
          <td>{{ order.eventTitle }}</td>
          <td>{{ order.date }}</td>
          <td>
              <span
                  :class="[
                  'status-badge',
                  order.status === '待审核'
                    ? 'badge-pending'
                    : order.status === '已通过'
                    ? 'badge-pass'
                    : order.status === '已拒绝'
                    ? 'badge-reject'
                    : ''
                ]"
              >
                {{ order.status }}
              </span>
          </td>
          <td>
            <template v-if="order.status === '待审核'">
              <button class="btn-pass" @click="approveOrder(order.id)">通过</button>
              <button class="btn-reject" @click="rejectOrder(order.id)">拒绝</button>
            </template>
            <span v-else style="color:#ccc;">--</span>
          </td>
        </tr>
        <tr v-if="orders.length === 0">
          <td colspan="5" style="color:#bbb;padding:2em;">暂无数据</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const orders = ref([])
const filters = ref({
  username: '',
  date: '',
})

const user = JSON.parse(localStorage.getItem('user') || '{}')

async function fetchOrders() {
  try {
    const res = await axios.get('/api/appointments', {
      params: filters.value,
      headers: { 'X-Role': user.role || '' },
    })
    orders.value = res.data.map(item => ({
      id: item.id,
      username: item.username,
      eventTitle: item.activityName || item.eventTitle || '',
      date: item.startTime || item.date || '',
      status: item.status
    }))
  } catch (error) {
    alert('获取预约记录失败，请刷新页面重试')
    console.error(error)
  }
}

async function approveOrder(id) {
  try {
    await axios.put(`/api/appointments/${id}/approve`, {}, {
      headers: { 'X-Role': user.role || '' },
    })
    fetchOrders()
  } catch (error) {
    alert('审批失败')
    console.error(error)
  }
}
async function rejectOrder(id) {
  try {
    await axios.put(`/api/appointments/${id}/reject`, {}, {
      headers: { 'X-Role': user.role || '' },
    })
    fetchOrders()
  } catch (error) {
    alert('拒绝失败')
    console.error(error)
  }
}

onMounted(fetchOrders)
</script>

<style scoped>
.admin-main {
  padding: 32px 0 0;
  min-height: 80vh;
  background: linear-gradient(115deg, #f2f6fc 0%, #e5f5fe 100%);
}
.search-box {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(93,142,217,0.06);
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 20px 32px 16px 32px;
  width: 95%;
  max-width: 820px;
  margin: 28px auto 18px auto;
}
.search-box input[type='text'],
.search-box input[type='date'] {
  padding: 7px 14px;
  font-size: 1em;
  border: 1px solid #d3e0ee;
  border-radius: 7px;
  outline: none;
  transition: border 0.2s;
  background: #f9fbfe;
}
.search-box input:focus {
  border: 1.5px solid #327cf4;
  background: #f3fafa;
}
.search-box button {
  background: linear-gradient(90deg, #2196f3 0%, #68c1fe 100%);
  color: #fff;
  border-radius: 7px;
  padding: 7px 28px;
  border: none;
  font-size: 1em;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 2px 4px #b1c1f838;
  transition: background 0.18s;
}
.search-box button:hover {
  background: #1b77e3;
}

.table-wrap {
  width: 95%;
  max-width: 980px;
  margin: 0 auto 50px auto;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 6px 28px #dbe7e4ae;
  padding: 15px 8px 18px 8px;
}
.order-table {
  width: 100%;
  border-collapse: collapse;
  background:transparent;
}
.order-table th {
  background: #e8f0fc;
  color: #105c9e;
  font-weight: 600;
  padding: 14px 0;
  font-size: 1em;
  border-bottom: 1.5px solid #d5e1f0;
}
.order-table tr {
  transition: background 0.14s;
}
.order-table tbody tr:nth-child(odd) {
  background: #f7fbff;
}
.order-table tbody tr:hover {
  background: #e2ecfd;
}
.order-table td {
  padding: 15px 5px;
  color: #25669a;
  font-size: 0.98em;
  border-bottom: 1px solid #e5eaea;
}
.status-badge {
  display: inline-block;
  min-width: 58px;
  font-weight: bold;
  padding: 3px 13px;
  border-radius: 13px;
  font-size: 0.95em;
}
.badge-pending {
  background: #fffae6;
  color: #ef9706;
  border: 1.5px solid #ffe263;
}
.badge-pass {
  background: #e9faec;
  color: #15bd52;
  border: 1.5px solid #9beab1;
}
.badge-reject {
  background: #fbebe9;
  color: #e64343;
  border: 1.5px solid #f5b8b2;
}
.btn-pass,
.btn-reject {
  border: none;
  font-weight: 500;
  border-radius: 7px;
  padding: 7px 20px;
  margin: 0 6px;
  font-size: 0.98em;
  cursor: pointer;
  transition: background 0.18s;
}
.btn-pass {
  background: #43e36b2c;
  color: #0ab26b;
  border: 1.5px solid #5be6a1;
}
.btn-pass:hover {
  background: #12c570;
  color: #fff;
}
.btn-reject {
  background: #fad9d9;
  color: #e94c30;
  border: 1.5px solid #ffb0a8;
}
.btn-reject:hover {
  background: #ff6161e3;
  color: #fff;
}
@media (max-width:680px) {
  .search-box, .table-wrap {
    width: 99%;
    padding-left: 4px;
    padding-right: 4px;
    font-size: 0.97em;
  }
  .order-table th, .order-table td {
    padding: 7px 2px;
    font-size: 0.98em;
  }
}
</style>