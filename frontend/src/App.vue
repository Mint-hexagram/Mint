<template>
  <!-- 登录页面不显示侧边栏和头部 -->
  <router-view v-if="$route.path === '/login'" />
  
  <!-- 其他页面显示完整布局 -->
  <el-container v-else style="height: 100vh;">
    <el-aside width="220px">
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" router>
        <el-menu-item index="/">数据大屏</el-menu-item>
        <el-menu-item index="/task">任务管理</el-menu-item>
        <el-menu-item index="/defect">缺陷管理</el-menu-item>
        <el-menu-item index="/profile">个人中心</el-menu-item>
        <el-menu-item index="/system">系统管理</el-menu-item>
        <el-menu-item index="/log">系统日志</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#409EFF;color:#fff;font-size:22px;display:flex;justify-content:space-between;align-items:center;">
        <span>地铁隧道巡线车(AGV)作业数据云平台</span>
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              {{ userInfo.nickname || userInfo.username || '用户' }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main style="background:#f5f7fa;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import { logout } from './api/auth'

const route = useRoute()
const router = useRouter()
const activeMenu = ref(route.path)

// 用户信息
const userInfo = ref({
  username: '',
  nickname: ''
})

// 监听路由变化
watch(() => route.path, (val) => {
  activeMenu.value = val
})

// 初始化用户信息
onMounted(() => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      userInfo.value = JSON.parse(userInfoStr)
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
})

// 处理下拉菜单命令
const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      // 调用登出API
      await logout()
      
      // 清除本地存储
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      
      ElMessage({
        message: '已退出登录',
        type: 'success',
        duration: 2000
      })
      
      // 跳转到登录页
      router.push('/login')
    } catch (error) {
      if (error !== 'cancel') {
        console.error('登出失败:', error)
        ElMessage({
          message: '登出失败',
          type: 'error',
          duration: 3000
        })
      }
    }
  }
}
</script>

<style>
body {
  margin: 0;
}

.el-header {
  line-height: 60px;
  text-align: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-dropdown {
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.user-dropdown:hover {
  opacity: 0.8;
}
</style> 