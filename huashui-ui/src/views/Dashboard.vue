<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { attendanceApi, dormitoryApi, messageApi } from '@/api'
import { useAuthStore } from '@/store/auth'
import campusImage from '@/assets/login-carousel/1552382688_5438_thumb.jpg'
import './dashboard.css'

const router = useRouter()
const auth = useAuthStore()
const stats = ref<any>(null)
const myDorm = ref<any>(null)
const notices = ref<any[]>([])
const loading = ref(false)

const showDormitory = computed(() => auth.profile?.userType === 'STUDENT')
const displayName = computed(() => auth.profile?.realName || auth.profile?.username || '华水用户')
const statItems = computed(() => [
  { label: '正常考勤', value: stats.value?.normal ?? '—' },
  { label: '迟到记录', value: stats.value?.late ?? '—' },
  { label: '缺勤记录', value: stats.value?.absent ?? '—' },
  { label: '请假记录', value: stats.value?.leave ?? '—' }
])

async function load() {
  loading.value = true
  try {
    const role = auth.profile?.userType
    const statsRequest = ['SUPER_ADMIN', 'DORM_MANAGER'].includes(role || '')
      ? attendanceApi.statistics({})
      : Promise.resolve({ data: null })
    const dormRequest = role === 'STUDENT'
      ? dormitoryApi.myDorm()
      : Promise.resolve({ data: null })
    const [statsRes, dormRes, noticeRes] = await Promise.allSettled([
      statsRequest,
      dormRequest,
      messageApi.noticeLatest()
    ])
    stats.value = statsRes.status === 'fulfilled' ? statsRes.value?.data : null
    myDorm.value = dormRes.status === 'fulfilled' ? dormRes.value?.data : null
    notices.value = noticeRes.status === 'fulfilled' ? (noticeRes.value?.data || []).slice(0, 5) : []
  } finally {
    loading.value = false
  }
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 16) : ''
}

onMounted(load)
</script>

<template>
  <section class="dashboard hs-page" v-loading="loading" aria-label="校园生活总览">
    <div class="dashboard-summary">
      <div class="dashboard-welcome hs-panel">
        <div>
          <div class="dashboard-welcome__eyebrow">今日工作台</div>
          <h2>{{ displayName }}，欢迎回来。</h2>
          <p>在这里查看校园服务概况，并继续处理你的宿舍、考勤、缴费和消息事项。</p>
        </div>
        <div class="dashboard-welcome__meta">
          <el-icon aria-hidden="true"><Location /></el-icon>
          <span>华北水利水电大学校园生活服务</span>
        </div>
      </div>
      <figure class="dashboard-campus hs-panel">
        <img :src="campusImage" alt="校园湖畔与教学楼" width="800" height="500" />
      </figure>
    </div>

    <div v-if="stats" class="stats-grid" aria-label="考勤概况">
      <article v-for="item in statItems" :key="item.label" class="stat-card hs-panel">
        <div class="stat-card__top"><span class="stat-card__label">{{ item.label }}</span></div>
        <div class="stat-card__value">{{ item.value }}</div>
      </article>
    </div>

    <div class="dashboard-panels">
      <section v-if="showDormitory" class="dashboard-panel hs-panel">
        <div class="panel-heading"><h3>我的宿舍</h3><span>当前住宿信息</span></div>
        <div v-if="myDorm" class="dormitory-list">
          <div class="dormitory-item"><div class="dormitory-item__label">校区</div><div class="dormitory-item__value">{{ myDorm.campusName || '—' }}</div></div>
          <div class="dormitory-item"><div class="dormitory-item__label">楼栋</div><div class="dormitory-item__value">{{ myDorm.buildingName || '—' }}</div></div>
          <div class="dormitory-item"><div class="dormitory-item__label">房间</div><div class="dormitory-item__value">{{ myDorm.roomNumber || '—' }}</div></div>
          <div class="dormitory-item"><div class="dormitory-item__label">床位</div><div class="dormitory-item__value">{{ myDorm.bedNumber || '—' }}</div></div>
        </div>
        <div v-else class="dashboard-empty">暂无宿舍信息</div>
      </section>

      <section class="dashboard-panel hs-panel" :class="{ 'dashboard-panel--wide': !showDormitory }">
        <div class="panel-heading"><h3>最新公告</h3><el-button link type="primary" @click="router.push('/notice/center')">查看全部</el-button></div>
        <div v-if="notices.length" class="notice-list">
          <button v-for="notice in notices" :key="notice.id" type="button" class="notice-row" @click="router.push('/notice/center')">
            <span class="notice-row__title">{{ notice.title }}</span>
            <time class="notice-row__time">{{ formatTime(notice.publishTime) }}</time>
          </button>
        </div>
        <div v-else class="dashboard-empty">暂无公告</div>
      </section>
    </div>
  </section>
</template>
