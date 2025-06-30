import request from './request'

// 用户登录
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

// 用户注册
export function register(data) {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  })
}

// 忘记密码 - 发送验证码
export function forgotPassword(data) {
  return request({
    url: '/api/auth/forgot-password',
    method: 'post',
    data
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/api/auth/reset-password',
    method: 'post',
    data
  })
}

// 用户登出
export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}

// 刷新令牌
export function refreshToken() {
  return request({
    url: '/api/auth/refresh',
    method: 'post'
  })
} 