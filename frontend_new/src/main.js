import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router)
app.mount('#app')
import { store } from './store'
const userStr = localStorage.getItem('currentUser')
if (userStr) {
    Object.assign(store.currentUser, JSON.parse(userStr))
}