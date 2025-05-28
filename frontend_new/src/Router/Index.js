import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Activity from '../views/Activity.vue'
import Register from '../views/Register.vue'
import Profile from '../views/Profile.vue'
import OrderList from '../views/OrderList.vue'
import NoticeList from '../views/NoticeList.vue'
import About from '../views/About.vue'
import Admin from '../components/Admin.vue'
import Appointments from '../views/Appointments.vue'
const routes = [
    { path: '/', redirect: '/activities' },
    { path: '/login', name: 'Login', component: Login },
    { path: '/Appointments', name: 'MyAppointments', component: Appointments},
    { path: '/orders', name: 'Orders', component: OrderList },
    { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAdmin: true } },
    { path: '/notices', name: 'Notices', component: NoticeList },
    { path: '/profile', name: 'Profile', component: Profile },
    { path: '/register', name: 'Register', component: Register },
    { path: '/activities', name: 'Activities', component: Activity },
    { path: '/about', name: 'About', component: About },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('../views/NotFound.vue') }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})
const whiteList = ['/login', '/register'];
router.beforeEach((to, from, next) => {
    console.log('导航到：', to.path)
    // 白名单直接放行
    if (whiteList.includes(to.path)) return next()
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    if (!user.username) return next('/login')

    // 管理员 — 只允许/admin
    if (user.role === 'admin') {
        if (to.path !== '/admin') return next('/admin')
        // 允许进入 /admin
        return next()
    }

    // 普通用户 — 禁止进入/admin
    if (to.meta.requiresAdmin && user.role !== 'admin') {
        alert('没有管理员权限！')
        return next('/login')
    }
    // 其它请求正常放行
    next()
})
export default router