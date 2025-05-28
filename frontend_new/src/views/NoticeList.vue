<template>
  <div class="notice-page ui-card">
    <h2 class="title">📢 平台公告</h2>
    <ul>
      <li v-for="n in store.notices" :key="n.id" class="notice-item">
        <div class="notice-title">{{ n.title }}</div>
        <div class="notice-date">{{ n.date }}</div>
        <div class="notice-content">{{ n.content }}</div>
      </li>
    </ul>
    <div v-if="store.notices.length === 0" class="empty">暂无公告</div>
  </div>
</template>
<script setup>
import { store } from '../store'
</script>
<style scoped>
.notice-page {
  max-width: 540px;
  margin: 60px auto;
  padding: 38px 34px;
  background: #fcfcfc;
  border-radius: 12px;
  box-shadow: 0 4px 24px #0001;
}
.title {
  font-size: 2rem;
  text-align: center;
  margin-bottom: 26px;
  letter-spacing: 2px;
  color: #3167b7;
  font-weight: bold;
}
.notice-item {
  list-style: none;
  border-bottom: 1px dashed #e0e7ef;
  margin-bottom: 18px;
  padding-bottom: 13px;
  padding-left: 4px;
  transition: background 0.2s;
}
.notice-item:last-child { border-bottom: none; }
.notice-title {
  font-size: 1.12rem;
  color: #23447D;
  font-weight: 600;
  margin-bottom: .4em;
}
.notice-date {
  font-size: 0.92em;
  color: #9ba5b5;
  margin-bottom: 0.14em;
}
.notice-content { color: #364150; }
.empty { text-align: center; color: #bbb; margin-top: 24px;}
</style>