import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { refreshToken } from './auth'

// 创建axios实例
const service = axios.create({
  // 支持HTTPS和HTTP
  baseURL: process.env.NODE_ENV === 'production' 
    ? 'https://localhost:8443' 
    : 'http://localhost:8080',
  timeout: 10000
})

// 是否正在刷新token
let isRefreshing = false
// 重试队列
let retryQueue = []

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
    if (res.code === 200) {
      return res
    } else {
      // 新增：如果是缺陷编号唯一约束冲突，不弹全局ElMessage，交由业务代码处理
      if (res.message && res.message.includes('Duplicate entry') && res.message.includes('defect_no')) {
        return Promise.reject(new Error(res.message))
      }
      ElMessage({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    }
  },
  async error => {
    console.log('err' + error)
    
    if (error.response) {
      const { status } = error.response
      
      if (status === 401) {
        // token过期或无效
        const token = localStorage.getItem('token')
        
        if (token && !isRefreshing) {
          isRefreshing = true
          
          try {
            // 尝试刷新token
            const refreshResponse = await refreshToken()
            if (refreshResponse.code === 200) {
              const newToken = refreshResponse.data
              localStorage.setItem('token', newToken)
              
              // 重试队列中的请求
              retryQueue.forEach(cb => cb(newToken))
              retryQueue = []
              
              // 重试当前请求
              error.config.headers['Authorization'] = `Bearer ${newToken}`
              return service(error.config)
            } else {
              // 刷新失败，跳转登录页
              handleAuthError()
            }
          } catch (refreshError) {
            // 刷新失败，跳转登录页
            handleAuthError()
          } finally {
            isRefreshing = false
          }
        } else if (isRefreshing) {
          // 将请求加入重试队列
          return new Promise(resolve => {
            retryQueue.push(token => {
              error.config.headers['Authorization'] = `Bearer ${token}`
              resolve(service(error.config))
            })
          })
        } else {
          // 没有token，直接跳转登录页
          handleAuthError()
        }
      } else if (status === 403) {
        ElMessage({
          message: '没有权限访问该资源',
          type: 'error',
          duration: 5 * 1000
        })
      } else {
        ElMessage({
          message: error.response.data?.message || '请求失败',
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else {
      ElMessage({
        message: '网络错误',
        type: 'error',
        duration: 5 * 1000
      })
    }
    
    return Promise.reject(error)
  }
)

// 处理认证错误
function handleAuthError() {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  
  // 如果当前不在登录页，则跳转到登录页
  if (router.currentRoute.value.path !== '/login') {
    router.push('/login')
  }
  
  ElMessage({
    message: '登录已过期，请重新登录',
    type: 'warning',
    duration: 5 * 1000
  })
}

export default service 