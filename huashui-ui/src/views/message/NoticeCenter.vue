<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { messageApi } from '@/api'
import './content-list.css'

const rows = ref<any[]>([])
const nextCursor = ref<number | null>(null)
const hasMore = ref(false)
const loading = ref(false)
const detail = ref<any>(null)
const detailVisible = ref(false)

async function load() {
  if (loading.value) return
  loading.value = true
  try {
    const params: any = { pageSize: 10 }
    if (nextCursor.value) params.cursor = nextCursor.value
    const res: any = await messageApi.noticeScroll(params)
    const data = res.data || {}
    rows.value = rows.value.concat(data.records || [])
    nextCursor.value = data.nextCursor || null
    hasMore.value = Boolean(data.hasMore)
  } finally {
    loading.value = false
  }
}

async function openDetail(id: number) {
  const res: any = await messageApi.noticeDetail(id)
  detail.value = res.data
  detailVisible.value = true
}

function formatTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 16) : ''
}

onMounted(load)
</script>

<template>
  <section class="hs-page" aria-label="通知中心">
    <div class="hs-panel">

      <div v-if="rows.length" class="content-list">
        <button v-for="notice in rows" :key="notice.id" type="button" class="content-row" @click="openDetail(notice.id)">
          <span class="content-row__copy">
            <span class="content-row__title">{{ notice.title }}</span>
            <span class="content-row__summary">{{ notice.summary || '暂无摘要' }}</span>
          </span>
          <time class="content-row__meta">{{ formatTime(notice.publishTime) }}</time>
        </button>
      </div>
      <el-empty v-else-if="!loading" description="暂无通知" />
      <div v-if="hasMore" class="load-more"><el-button :loading="loading" @click="load">加载更多</el-button></div>
    </div>

    <el-dialog v-model="detailVisible" :title="detail?.title" width="min(640px, calc(100vw - 32px))">
      <p v-if="detail" class="rich-content">{{ detail.content }}</p>
    </el-dialog>
  </section>
</template>
