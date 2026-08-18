<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { attendanceApi, dormitoryApi, messageApi } from '@/api'

const stats = ref<any>(null)
const myDorm = ref<any>(null)
const notices = ref<any[]>([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const [statsRes, dormRes, noticeRes] = await Promise.allSettled([
      attendanceApi.statistics({}),
      dormitoryApi.myDorm(),
      messageApi.noticeLatest()
    ])
    stats.value = statsRes.status === 'fulfilled' ? statsRes.value?.data : null
    myDorm.value = dormRes.status === 'fulfilled' ? dormRes.value?.data : null
    notices.value = noticeRes.status === 'fulfilled' ? (noticeRes.value?.data || []).slice(0, 5) : []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hs-page" v-loading="loading">
    <div class="hs-page-title">
      <div class="cn">总览</div>
      <div class="en">Overview</div>
      <div class="hs-waterline"></div>
    </div>

    <el-row :gutter="16">
      <el-col :span="6" v-for="item in [
        { label: '正常考勤', value: stats?.normal ?? '—', color: '#1d6a96' },
        { label: '迟到', value: stats?.late ?? '—', color: '#c85c40' },
        { label: '缺勤', value: stats?.absent ?? '—', color: '#8b2635' },
        { label: '请假', value: stats?.leave ?? '—', color: '#b08b5b' }
      ]" :key="item.label">
        <div class="stat-card hs-panel">
          <div class="stat-label">{{ item.label }}</div>
          <div class="stat-value" :style="{ color: item.color }">{{ item.value }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <div class="hs-panel">
          <h3>我的宿舍</h3>
          <el-descriptions v-if="myDorm" :column="2" border>
            <el-descriptions-item label="校区">{{ myDorm.campusName || '—' }}</el-descriptions-item>
            <el-descriptions-item label="楼栋">{{ myDorm.buildingName || '—' }}</el-descriptions-item>
            <el-descriptions-item label="房间">{{ myDorm.roomNumber || '—' }}</el-descriptions-item>
            <el-descriptions-item label="床位">{{ myDorm.bedNumber || '—' }}</el-descriptions-item>
          </el-descriptions>
          <el-empty v-else description="暂无宿舍信息" :image-size="70" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="hs-panel">
          <h3>最新通知</h3>
          <div v-if="notices.length" class="notice-list">
            <div v-for="notice in notices" :key="notice.id" class="notice-item">
              <span class="dot"></span>
              <span>{{ notice.title }}</span>
              <span class="time">{{ notice.publishTime?.replace('T', ' ').slice(0, 16) }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无通知" :image-size="70" />
        </div>
      </el-col>
    </el-row>
  </section>
</template>

<style scoped>
.stat-card {
  padding: 20px;
}
.stat-label {
  color: var(--hs-muted);
  font-size: 13px;
}
.stat-value {
  margin-top: 10px;
  font-size: 32px;
  font-weight: 900;
}
h3 {
  margin: 0 0 16px;
  color: var(--hs-deep);
  font-size: 16px;
}
.notice-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px dashed #e4e8ea;
}
.dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--hs-clay);
}
.time {
  margin-left: auto;
  color: var(--hs-muted);
  font-size: 12px;
}
</style>