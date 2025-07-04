<template>
  <div class="dashboard-bg">
    <div class="wireframe">
      <div class="header">
        <div class="title">地铁隧道巡线大数据仿真和分析平台</div>
        <div class="enter-btn">进入系统</div>
      </div>
      <div class="main-content">
        <!-- 左侧面板 -->
        <div class="left-panel">
          <div class="card">
            <div class="card-title">巡视数据统计</div>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.todayInspectionCount ?? '-') }}</div>
                <div class="stat-label">今日巡视数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.yesterdayInspectionCount ?? '-') }}</div>
                <div class="stat-label">昨日巡视数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.taskGrowthRate ?? '-') }}</div>
                <div class="stat-label">环比增长</div>
              </div>
            </div>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.todayInspectionDistance ?? '-') }}</div>
                <div class="stat-label">今日巡视距离</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.yesterdayInspectionDistance ?? '-') }}</div>
                <div class="stat-label">昨日巡视距离</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ loading ? '加载中' : (summary?.distanceGrowthRate ?? '-') }}</div>
                <div class="stat-label">环比增长</div>
              </div>
            </div>
          </div>
          <div class="card">
            <div class="card-title">缺陷类型统计</div>
            <div class="chart-area">饼状图 - 缺陷类型分布</div>
          </div>
          <div class="card">
            <div class="card-title">人员数据统计</div>
            <div class="chart-area">柱状图 - 人员任务统计</div>
          </div>
        </div>
        <!-- 中间面板 -->
        <div class="center-panel">
          <div class="center-stats">
            <div class="center-stats-row">
              <div class="center-stat">
                <div class="center-stat-number">1,234</div>
                <div class="center-stat-label">巡视总距离</div>
              </div>
              <div class="center-stat">
                <div class="center-stat-number">5,678</div>
                <div class="center-stat-label">巡视总次数</div>
              </div>
            </div>
          </div>
          <div class="earth-section">
            <div class="map-layers">
              <div>图层1: 路线图</div>
              <div>图层2: 地铁图</div>
              <div>图层3: 地图背景</div>
            </div>
            地图可视化区域<br />
            (地铁路线、巡视轨迹、实时位置)
          </div>
        </div>
        <!-- 右侧面板 -->
        <div class="right-panel">
          <div class="card">
            <div class="card-title">缺陷数据统计</div>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-number">456</div>
                <div class="stat-label">累计缺陷数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">321</div>
                <div class="stat-label">确认缺陷数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">135</div>
                <div class="stat-label">误报缺陷数</div>
              </div>
            </div>
          </div>
          <div class="card">
            <div class="card-title">每月巡检次数</div>
            <div class="chart-area">柱状图 - 月度巡检趋势</div>
          </div>
          <div class="card">
            <div class="card-title">缺陷数据变化</div>
            <div class="chart-area">折线图 - 缺陷趋势变化</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardSummary } from '../api/dashboard'
import { ElMessage } from 'element-plus'

const summary = ref(null)
const loading = ref(false)

function loadSummary() {
  loading.value = true
  getDashboardSummary().then(res => {
    if (res.code === 200) {
      summary.value = res.data
    } else {
      ElMessage.error(res.message || '获取仪表盘数据失败')
    }
  }).catch(error => {
    console.error('获取仪表盘数据错误:', error)
    ElMessage.error('获取仪表盘数据失败')
  }).finally(() => {
    loading.value = false
  })
}

onMounted(() => {
  loadSummary()
})
</script>

<style scoped>
.dashboard-bg {
  min-height: 100vh;
  background: #0f1419;
  color: white;
  padding: 20px;
  box-sizing: border-box;
}
.wireframe {
  border: 2px solid #00d4ff;
  background: rgba(0, 212, 255, 0.1);
  height: calc(100vh - 40px);
  position: relative;
}
.header {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #00d4ff;
  position: relative;
}
.title {
  font-size: 24px;
  font-weight: bold;
}
.enter-btn {
  position: absolute;
  top: 20px;
  right: 30px;
  border: 2px solid #00d4ff;
  padding: 10px 20px;
  background: rgba(0, 212, 255, 0.2);
  cursor: pointer;
}
.main-content {
  display: flex;
  height: calc(100% - 100px);
  gap: 20px;
  padding: 20px;
  overflow: hidden;
}
.left-panel, .right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: stretch;
}
.center-panel {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: stretch;
  justify-content: center;
  min-width: 400px;
}
.card {
  border: 1px solid #00d4ff;
  background: rgba(0, 212, 255, 0.05);
  padding: 15px;
  margin-bottom: 0;
  box-sizing: border-box;
}
.card-title {
  font-size: 16px;
  margin-bottom: 15px;
  text-align: center;
  border-bottom: 1px solid rgba(0, 212, 255, 0.3);
  padding-bottom: 10px;
}
.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
  margin-bottom: 15px;
}
.stat-item {
  text-align: center;
  border: 1px solid rgba(0, 212, 255, 0.3);
  padding: 10px 5px;
}
.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #00d4ff;
}
.stat-label {
  font-size: 12px;
  margin-top: 5px;
  color: #ccc;
}
.card .chart-area {
  flex: 1 1 auto;
  min-height: 120px;
  max-height: 260px;
  border: 1px solid rgba(0, 212, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 212, 255, 0.02);
  color: #666;
  box-sizing: border-box;
  overflow: hidden;
}
.center-stats {
  border: 1px solid #00d4ff;
  background: rgba(0, 212, 255, 0.1);
  padding: 20px;
  text-align: center;
  margin-bottom: 0;
}
.center-stats-row {
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px;
}
.center-stat {
  text-align: center;
}
.center-stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #00d4ff;
}
.center-stat-label {
  font-size: 14px;
  color: #ccc;
  margin-top: 5px;
}
.earth-section {
  flex: none;
  border: 1px solid #00d4ff;
  background: rgba(0, 212, 255, 0.02);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #666;
  position: relative;
  height: 380px;
  margin-bottom: 0;
  box-shadow: 0 0 24px 0 rgba(0,212,255,0.08);
}
.map-layers {
  position: absolute;
  top: 20px;
  left: 20px;
  font-size: 12px;
  color: #999;
}
.left-panel .card, .right-panel .card {
  display: flex;
  flex-direction: column;
  flex: 1 1 0;
}
.left-panel, .right-panel {
  height: 100%;
}
</style> 