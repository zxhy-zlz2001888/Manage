<template>
  <div class="admin-panel">
    <h2>管理员后台</h2>

    <div class="tabs">
      <button :class="{active: tab==='user'}" @click="tab='user'">用户管理</button>
      <button :class="{active: tab==='activity'}" @click="tab='activity'">活动/服务管理</button>
      <button :class="{active: tab==='order'}" @click="tab='order'">预约记录管理</button>
    </div>

    <div v-if="tab==='user'">
      <UserManage />
    </div>

    <div v-if="tab==='activity'">
      <ActivityList :isAdmin="true"/>
    </div>

    <div v-if="tab==='order'">
      <OrderManage />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import UserManage from './UserManage.vue'
import OrderManage from './OrderManage.vue'
import ActivityList from './ActivityList.vue'

const tab = ref('user')
</script>

<style scoped>
.tabs { margin-bottom: 18px; text-align: center; }
.tabs button { margin: 0 10px; padding: 8px 25px; border-radius: 7px; background: #f5f6fa; color: #4473c6; font-size: 1.02em;}
.tabs button.active { background: #3478f6; color: #fff; }
</style>