// 简单的数据模拟
import { reactive } from 'vue'

export const store = reactive({
    isLoggedIn: false,

    nextProductId: 3,
    currentUser: {
        username: '',
        gender: '',
        origin: '',
        hobbies: [],
        intro: '',
        phone: '',  // 新增手机号
        email: '',  // 新增联系邮箱
    },

        orders: [], // {id, productId, time}
        notices: [
            { id: 1, title: '🎉新产品上线', content: '欢迎体验全新产品功能！', date: "2025-06-12" },
            { id: 2, title: '维护通知', content: '系统将于6月18日凌晨维护!', date: "2025-06-11" }
        ]


})


