<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { messageApi } from '@/api'
import './content-list.css'

const rows = ref<any[]>([])
const unread = ref(0)
const nextCursor = ref<number | null>(null)
const hasMore = ref(false)
const loading = ref(false)
const detail = ref<any>(null)
const detailVisible = ref(false)

async function loadUnread() {
  const res: any = await messageApi.unreadCount()
  unread.value = Number(res.data || 0)
}

async function load() {
  if (loading.value) return
  loading.value = true
  try {
    const params: any = { limit: 20 }
    if (nextCursor.value) params.cursor = nextCursor.value
    const res: any = await messageApi.messageScroll(params)
    const data = res.data || {}
    rows.value = rows.value.concat(data.records || [])
    nextCursor.value = data.nextCursor || null
    hasMore.value = Boolean(data.hasMore)
  } finally {
    loading.value = false
  }
}

async function openDetail(id: number) {
  const res: any = await messageApi.messageDetail(id)
  detail.value = res.data
  detailVisible.value = true
  loadUnread()
}

async function readAll() {
  await messageApi.readAll()
  unread.value = 0
  rows.value = []
  nextCursor.value = null
  await load()
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 16) : ''
}

onMounted(() => {
  loadUnread()
  load()
})
</script>

<template>
  <section class="hs-page" aria-label="我的消息">
    <div class="hs-panel">
      <div class="list-toolbar">
        <span class="list-toolbar__status">{{ unread ? `${unread} 条未读消息` : '消息已全部读完' }}</span>
        <el-button :disabled="unread === 0" @click="readAll">全部标为已读</el-button>
      </div>
      <div v-if="rows.length" class="content-list">
        <button v-for="message in rows" :key="message.id" type="button" class="content-row" @click="openDetail(message.id)">
          <span class="content-row__copy">
            <span class="content-row__title">{{ message.title }}</span>
            <span class="content-row__summary">{{ message.content || message.type || '暂无内容' }}</span>
          </span>
          <time class="content-row__meta">{{ formatTime(message.createTime) }}</time>
        </button>
      </div>
      <el-empty v-else-if="!loading" description="暂无消息" />
      <div v-if="hasMore" class="load-more"><el-button :loading="loading" @click="load">加载更多</el-button></div>
    </div>

    <el-dialog v-model="detailVisible" :title="detail?.title" width="min(560px, calc(100vw - 32px))">
      <div v-if="detail">
        <p class="rich-content">{{ detail.content }}</p>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="类型">{{ detail.type }}</el-descriptions-item>
          <el-descriptions-item label="优先级">{{ detail.priority }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.status }}</el-descriptions-item>
          <el-descriptions-item label="时间">{{ detail.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </section>
</template>
