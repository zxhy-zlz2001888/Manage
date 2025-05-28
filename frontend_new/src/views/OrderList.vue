<template>
  <div class="order-container ui-card">
    <h2 class="title">🗂️ 我的预约/报名列表</h2>
    <ul>
      <li
          v-for="order in store.orders"
          :key="order.id"
          class="order-item"
          :class="order.status"
      >
        <div class="order-main">
          <span class="order-time">{{ order.time }}</span>
          •
          <span class="order-product">{{ getProductName(order.productId) }}</span>
        </div>
        <div class="order-extra">
          <span class="order-status" :class="order.status">{{ getStatusText(order.status) }}</span>
          <button
              class="order-cancel"
              v-if="canCancel(order.status)"
              @click="cancelOrder(order.id)"
          >取消报名</button>
        </div>
        <div class="order-feedback" v-if="order.feedback">
          <span>备注/反馈：{{ order.feedback }}</span>
        </div>
      </li>
    </ul>
    <div v-if="!store.orders.length" class="empty">暂无预约/报名~</div>
  </div>
</template>

<script setup>
import { store } from '../store'
import { ref } from 'vue'

// 状态文本映射
function getStatusText(status){
  switch(status){
    case 'pending': return '待审核'
    case 'approved': return '已通过'
    case 'rejected': return '已拒绝'
    default: return '未知'
  }
}
function getProductName(productId){
  const prod = store.products.find(p=>p.id===productId)
  return prod ? prod.name : '未知活动'
}

// 是否可取消
function canCancel(status){
  // 这里只允许待审核/已通过的单可取消
  return status === 'pending' || status === 'approved'
}

// 模拟取消操作
function cancelOrder(orderId){
  if(confirm('确定要取消该报名吗？')){
    // 你可根据接口实际操作
    const idx = store.orders.findIndex(o=>o.id===orderId)
    if(idx!==-1){
      store.orders.splice(idx, 1)
      alert('已取消报名')
    }
  }
}
</script>

<style scoped>
.order-container {
  max-width: 480px;
  margin: 60px auto;
  background: #fcfcfc;
  border-radius: 12px;
  box-shadow: 0 4px 24px #0001;
  padding: 36px 30px;
}
.title {
  font-size: 1.6rem;
  text-align: center;
  margin-bottom: 18px;
  color: #3f59a3;
  font-weight: bold;
}
ul { margin: 0; padding: 0;}
.order-item {
  list-style: none;
  margin-bottom: 18px;
  padding: 12px 0 12px 8px;
  border-left: 3px solid #437cf9;
  background: #f8faff;
  border-radius: 5px;
  box-shadow: 0 1px 2px #c6d1ee11;
  line-height: 1.5;
}
.order-item:last-child { margin-bottom: 0; }
.order-main { font-size: 1.08em; }
.order-time { color: #6788c8; margin-right: 5px;}
.order-product { color: #153a80; font-weight: 500;}
.order-status {
  border-radius: 4px;
  padding: 1px 7px;
  font-size: .98em;
  margin-left: 6px;
}
.order-status.pending { color: #f2690b; background: #ffe8cc; }
.order-status.approved { color: #19995a; background: #edf8ef;}
.order-status.rejected { color: #b53131; background: #ffe8e3; }

.order-extra {
  display: flex;
  align-items: center;
  margin-top: 7px;
}
.order-cancel {
  margin-left: 15px;
  background: #f6f6f6;
  border: 1px solid #d8d8d8;
  color: #b53131;
  padding: 3px 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: background .2s;
  font-size: .98em;
}
.order-cancel:hover { background: #ffeaea; }
.order-feedback {
  font-size: .95em;
  color: #665;
  margin-top: 6px;
  padding-left: 2px;
}
.empty { text-align: center; color: #bbb; margin-top: 36px; }
</style>