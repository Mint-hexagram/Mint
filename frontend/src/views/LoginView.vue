<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>地铁隧道巡线大数据仿真和分析平台</h2>
        <p>Metro Tunnel Inspection Cloud Platform</p>
      </div>
      
      <!-- 登录表单 -->
      <el-form
        v-if="!isRegister && !isForgotPassword"
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            prefix-icon="User"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input
              v-model="loginForm.captcha"
              placeholder="请输入验证码"
              prefix-icon="Key"
              size="large"
              class="captcha-input"
            />
            <div class="captcha-image" @click="refreshCaptcha">
              <span>{{ captchaText }}</span>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          <span>还没有账号？</span>
          <a @click="switchToRegister">立即注册</a>
          <span class="divider">|</span>
          <a @click="switchToForgotPassword">忘记密码？</a>
        </div>
      </el-form>
      
      <!-- 注册表单 -->
      <el-form
        v-else-if="isRegister"
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="login-form"
        @submit.prevent="handleRegister"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入昵称"
            prefix-icon="UserFilled"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            prefix-icon="Phone"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          <span>已有账号？</span>
          <a @click="switchToLogin">立即登录</a>
        </div>
      </el-form>
      
      <!-- 忘记密码表单 -->
      <el-form
        v-else-if="isForgotPassword"
        ref="forgotPasswordFormRef"
        :model="forgotPasswordForm"
        :rules="forgotPasswordRules"
        class="login-form"
        @submit.prevent="handleForgotPassword"
      >
        <el-form-item prop="username">
          <el-input
            v-model="forgotPasswordForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input
            v-model="forgotPasswordForm.phone"
            placeholder="请输入手机号"
            prefix-icon="Phone"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleForgotPassword"
          >
            {{ loading ? '发送中...' : '发送验证码' }}
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          <span>想起密码了？</span>
          <a @click="switchToLogin">立即登录</a>
        </div>
      </el-form>
      
      <!-- 重置密码表单 -->
      <el-form
        v-else-if="isResetPassword"
        ref="resetPasswordFormRef"
        :model="resetPasswordForm"
        :rules="resetPasswordRules"
        class="login-form"
        @submit.prevent="handleResetPassword"
      >
        <el-form-item prop="verificationCode">
          <el-input
            v-model="resetPasswordForm.verificationCode"
            placeholder="请输入验证码"
            prefix-icon="Key"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="newPassword">
          <el-input
            v-model="resetPasswordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="resetPasswordForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleResetPassword"
          >
            {{ loading ? '重置中...' : '重置密码' }}
          </el-button>
        </el-form-item>
        
        <div class="register-link">
          <span>想起密码了？</span>
          <a @click="switchToLogin">立即登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register, forgotPassword, resetPassword } from '../api/auth'

const router = useRouter()

// 表单引用
const loginFormRef = ref()
const registerFormRef = ref()
const forgotPasswordFormRef = ref()
const resetPasswordFormRef = ref()

// 加载状态
const loading = ref(false)

// 当前显示的表单类型
const currentForm = ref('login') // login, register, forgotPassword, resetPassword

// 记住密码
const rememberPassword = ref(false)

// 验证码
const captchaText = ref('')

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  captcha: ''
})

// 注册表单数据
const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  phone: ''
})

// 忘记密码表单数据
const forgotPasswordForm = reactive({
  username: '',
  phone: ''
})

// 重置密码表单数据
const resetPasswordForm = reactive({
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})

// 登录表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码长度为4位', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 忘记密码表单验证规则
const forgotPasswordRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 重置密码表单验证规则
const resetPasswordRules = {
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetPasswordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 计算属性
const isRegister = computed(() => currentForm.value === 'register')
const isForgotPassword = computed(() => currentForm.value === 'forgotPassword')
const isResetPassword = computed(() => currentForm.value === 'resetPassword')

// 生成验证码
const generateCaptcha = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
  let result = ''
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  captchaText.value = result
}

// 刷新验证码
const refreshCaptcha = () => {
  generateCaptcha()
  loginForm.captcha = ''
}

// 切换到注册
const switchToRegister = () => {
  currentForm.value = 'register'
  loginForm.captcha = ''
}

// 切换到登录
const switchToLogin = () => {
  currentForm.value = 'login'
  Object.keys(registerForm).forEach(key => {
    registerForm[key] = ''
  })
  Object.keys(forgotPasswordForm).forEach(key => {
    forgotPasswordForm[key] = ''
  })
  Object.keys(resetPasswordForm).forEach(key => {
    resetPasswordForm[key] = ''
  })
}

// 切换到忘记密码
const switchToForgotPassword = () => {
  currentForm.value = 'forgotPassword'
  loginForm.captcha = ''
}

// 切换到重置密码
const switchToResetPassword = () => {
  currentForm.value = 'resetPassword'
}

// 处理登录
const handleLogin = async () => {
  try {
    // 表单验证
    await loginFormRef.value.validate()
    
    // 验证验证码
    if (loginForm.captcha.toUpperCase() !== captchaText.value) {
      ElMessage({
        message: '验证码错误',
        type: 'error',
        duration: 3000
      })
      return
    }
    
    loading.value = true
    
    // 调用登录API
    const response = await login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    if (response.code === 200) {
      const { token, userId, username, nickname, roles, permissions } = response.data
      
      // 保存token和用户信息到localStorage
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify({
        userId,
        username,
        nickname,
        roles,
        permissions
      }))
      
      // 记住密码
      if (rememberPassword.value) {
        localStorage.setItem('rememberedUser', JSON.stringify({
          username: loginForm.username,
          password: loginForm.password
        }))
      } else {
        localStorage.removeItem('rememberedUser')
      }
      
      ElMessage({
        message: '登录成功',
        type: 'success',
        duration: 2000
      })
      
      // 跳转到首页
      router.push('/')
    } else {
      ElMessage({
        message: response.message || '登录失败',
        type: 'error',
        duration: 3000
      })
      // 刷新验证码
      refreshCaptcha()
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage({
      message: error.message || '登录失败，请检查用户名和密码',
      type: 'error',
      duration: 3000
    })
    // 刷新验证码
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  try {
    // 表单验证
    await registerFormRef.value.validate()
    
    loading.value = true
    
    // 调用注册API
    const response = await register({
      username: registerForm.username,
      nickname: registerForm.nickname,
      password: registerForm.password,
      phone: registerForm.phone
    })
    
    if (response.code === 200) {
      ElMessage({
        message: '注册成功，请登录',
        type: 'success',
        duration: 2000
      })
      
      // 切换到登录页面
      switchToLogin()
      
      // 填充用户名
      loginForm.username = registerForm.username
    } else {
      ElMessage({
        message: response.message || '注册失败',
        type: 'error',
        duration: 3000
      })
    }
  } catch (error) {
    console.error('注册错误:', error)
    ElMessage({
      message: error.message || '注册失败',
      type: 'error',
      duration: 3000
    })
  } finally {
    loading.value = false
  }
}

// 处理忘记密码
const handleForgotPassword = async () => {
  try {
    // 表单验证
    await forgotPasswordFormRef.value.validate()
    
    loading.value = true
    
    // 调用忘记密码API
    const response = await forgotPassword({
      username: forgotPasswordForm.username,
      phone: forgotPasswordForm.phone
    })
    
    if (response.code === 200) {
      ElMessage({
        message: '验证码已发送，请查看控制台输出',
        type: 'success',
        duration: 3000
      })
      
      // 切换到重置密码页面
      switchToResetPassword()
    } else {
      ElMessage({
        message: response.message || '发送失败',
        type: 'error',
        duration: 3000
      })
    }
  } catch (error) {
    console.error('忘记密码错误:', error)
    ElMessage({
      message: error.message || '发送失败',
      type: 'error',
      duration: 3000
    })
  } finally {
    loading.value = false
  }
}

// 处理重置密码
const handleResetPassword = async () => {
  try {
    // 表单验证
    await resetPasswordFormRef.value.validate()
    
    loading.value = true
    
    // 调用重置密码API
    const response = await resetPassword({
      verificationCode: resetPasswordForm.verificationCode,
      newPassword: resetPasswordForm.newPassword,
      confirmPassword: resetPasswordForm.confirmPassword
    })
    
    if (response.code === 200) {
      ElMessage({
        message: '密码重置成功，请登录',
        type: 'success',
        duration: 2000
      })
      
      // 切换到登录页面
      switchToLogin()
    } else {
      ElMessage({
        message: response.message || '重置失败',
        type: 'error',
        duration: 3000
      })
    }
  } catch (error) {
    console.error('重置密码错误:', error)
    ElMessage({
      message: error.message || '重置失败',
      type: 'error',
      duration: 3000
    })
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted(() => {
  // 生成验证码
  generateCaptcha()
  
  // 检查是否有记住的用户信息
  const rememberedUser = localStorage.getItem('rememberedUser')
  if (rememberedUser) {
    try {
      const user = JSON.parse(rememberedUser)
      loginForm.username = user.username
      loginForm.password = user.password
      rememberPassword.value = true
    } catch (error) {
      console.error('解析记住的用户信息失败:', error)
    }
  }
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.login-header p {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-top: 20px;
}

.captcha-container {
  display: flex;
  gap: 10px;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  width: 120px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  user-select: none;
}

.captcha-image:hover {
  background: #e8e8e8;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.login-button:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

.register-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.register-link a {
  color: #409EFF;
  cursor: pointer;
  text-decoration: none;
  margin-left: 5px;
}

.register-link a:hover {
  text-decoration: underline;
}

:deep(.el-input__wrapper) {
  height: 45px;
}

:deep(.el-input__inner) {
  height: 45px;
  font-size: 14px;
}
</style> 