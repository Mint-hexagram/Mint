<template>
  <div>
    <el-card>
      <el-table :data="[]" border>
        <el-table-column prop="id" label="日志编号" width="120" />
        <el-table-column prop="user" label="操作用户" width="120" />
        <el-table-column prop="action" label="操作内容" />
        <el-table-column prop="time" label="时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
</script> 