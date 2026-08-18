<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { messageApi } from '@/api'

const rows=ref<any[]>([]); const nextCursor=ref<number|null>(null); const hasMore=ref(false); const loading=ref(false)
const detail=ref<any>(null); const detailVisible=ref(false)

async function load(){loading.value=true;try{const params:any={pageSize:10};if(nextCursor.value)params.cursor=nextCursor.value;const res:any=await messageApi.noticeScroll(params);const data=res.data;rows.value=rows.value.concat(data.records||[]);nextCursor.value=data.nextCursor||null;hasMore.value=data.hasMore||false}finally{loading.value=false}}
async function openDetail(id:number){const res:any=await messageApi.noticeDetail(id);detail.value=res.data;detailVisible.value=true}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">通知中心</div><div class="en">Notices</div><div class="hs-waterline"></div></div><div class="hs-panel"><div v-for="n in rows" :key="n.id" class="notice-card" @click="openDetail(n.id)"><div class="notice-title">{{ n.title }}</div><div class="notice-summary">{{ n.summary }}</div><div class="notice-meta">{{ n.publishTime }}</div></div><el-empty v-if="!rows.length && !loading" description="暂无通知"/><el-button v-if="hasMore" text type="primary" @click="load">加载更多</el-button></div><el-dialog v-model="detailVisible" :title="detail?.title" width="640px"><div v-if="detail" v-html="detail.content"></div></el-dialog></section></template>
<style scoped>.notice-card{border-bottom:1px solid #e5eaec;padding:14px 0;cursor:pointer}.notice-title{font-weight:700;color:var(--hs-deep)}.notice-summary{color:var(--hs-muted);margin-top:4px}.notice-meta{font-size:12px;color:var(--hs-muted);margin-top:4px}</style>