<template>
  <div>
    <el-card>
      <el-form label-width="80px" style="max-width: 400px;">
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
</script> 