<template>
  <div class="profile-center">
    <el-button class="back-btn" type="text" @click="goBack">
      <el-icon style="vertical-align: middle;"><arrow-left /></el-icon>
      返回
    </el-button>
    <el-card>
      <el-form label-width="80px" style="max-width: 400px; margin: 0 auto;">
        <el-form-item label="用户名">
          <el-input value="admin" disabled />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
const router = useRouter()
const goBack = () => {
  router.push('/')
}
</script>
<style scoped>
.profile-center {
  min-height: 80vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}
.back-btn {
  position: absolute;
  top: 0;
  left: 0;
  font-size: 16px;
  color: #409EFF;
}
</style> 