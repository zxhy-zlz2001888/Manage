import axios from 'axios'

// 创建axios实例
const instance = axios.create({
    baseURL: '/api',   // 根据你的接口根路径填写
    timeout: 10000     // 请求超时时间
})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 自动附加用户角色或token
        const str = localStorage.getItem('user')
        if (str) {
            const user = JSON.parse(str)
            if (user.role) {
                config.headers['X-Role'] = user.role  // 示例
            }
            // 如果有token，也可以加上
            // if(user.token) config.headers['Authorization'] = 'Bearer ' + user.token
        }
        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
instance.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                window.alert('无权限或登录失效，请重新登录')
                localStorage.removeItem('user')
                window.location.href = '/login'
            }
        }
        return Promise.reject(error)
    }
)

export default instance