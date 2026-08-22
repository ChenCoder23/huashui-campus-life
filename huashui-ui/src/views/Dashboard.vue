<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { attendanceApi, dormitoryApi, messageApi } from '@/api'
import { useAuthStore } from '@/store/auth'
import campusBanner from '@/assets/login-carousel/1552382688_5438_thumb.jpg'

const auth = useAuthStore()
const stats = ref<any>(null)
const myDorm = ref<any>(null)
const notices = ref<any[]>([])
const loading = ref(false)

const isSuperAdmin = computed(() => auth.profile?.userType === 'SUPER_ADMIN')

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

    <div class="dashboard-banner hs-panel">
      <img :src="campusBanner" alt="校园风光" />
      <div class="banner-text">
        <strong>华水校园生活服务平台</strong>
        <span>宿舍 · 报修 · 考勤 · 缴费 · 通知</span>
      </div>
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
      <el-col v-if="!isSuperAdmin" :span="12">
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

      <el-col :span="isSuperAdmin ? 24 : 12">
        <div class="hs-panel notice-panel">
          <h3>学校最新公告</h3>
          <el-carousel v-if="notices.length" height="210px" trigger="click" :interval="5000" arrow="hover" indicator-position="outside">
            <el-carousel-item v-for="notice in notices" :key="notice.id">
              <div class="notice-slide">
                <div class="notice-title">{{ notice.title }}</div>
                <div class="notice-summary">{{ notice.summary || notice.content || '暂无内容' }}</div>
                <div class="notice-time">{{ notice.publishTime?.replace('T', ' ').slice(0, 16) }}</div>
              </div>
            </el-carousel-item>
          </el-carousel>
          <el-empty v-else description="暂无公告" :image-size="70" />
        </div>
      </el-col>
    </el-row>
  </section>
</template>

<style scoped>
.dashboard-banner {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 16px;
  padding: 14px;
}
.dashboard-banner img {
  width: 180px;
  height: 90px;
  object-fit: cover;
  border-radius: 10px;
}
.banner-text {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.banner-text strong {
  font-size: 20px;
  color: var(--hs-deep);
}
.banner-text span {
  color: var(--hs-muted);
}
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
.notice-panel :deep(.el-carousel__item) {
  background: linear-gradient(135deg, rgba(29, 106, 150, 0.06), rgba(232, 213, 183, 0.18));
  border-radius: 10px;
}
.notice-slide {
  height: 100%;
  padding: 22px 26px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.notice-title {
  font-size: 18px;
  font-weight: 800;
  color: var(--hs-deep);
  margin-bottom: 12px;
}
.notice-summary {
  color: var(--hs-ink);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.notice-time {
  margin-top: 10px;
  color: var(--hs-muted);
  font-size: 12px;
}
</style>
