<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { messageApi } from '@/api'

const rows=ref<any[]>([]); const unread=ref(0); const nextCursor=ref<number|null>(null); const hasMore=ref(false); const loading=ref(false)
const detail=ref<any>(null); const detailVisible=ref(false)

async function loadUnread(){const res:any=await messageApi.unreadCount();unread.value=res.data||0}
async function load(){loading.value=true;try{const params:any={limit:20};if(nextCursor.value)params.cursor=nextCursor.value;const res:any=await messageApi.messageScroll(params);const data=res.data;rows.value=rows.value.concat(data.records||[]);nextCursor.value=data.nextCursor||null;hasMore.value=data.hasMore||false}finally{loading.value=false}}
async function openDetail(id:number){const res:any=await messageApi.messageDetail(id);detail.value=res.data;detailVisible.value=true;loadUnread()}
async function readAll(){await messageApi.readAll();loadUnread();rows.value=[];nextCursor.value=null;load()}
onMounted(()=>{loadUnread();load()})
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">我的消息</div><div class="en">Inbox</div><div class="hs-waterline"></div></div><div class="hs-panel"><div class="inbox-bar"><el-tag type="danger">未读 {{ unread }}</el-tag><el-button text type="primary" @click="readAll">全部已读</el-button></div><div v-for="m in rows" :key="m.id" class="msg-card" @click="openDetail(m.id)"><div class="msg-title">{{ m.title }}</div><div class="msg-type">{{ m.type }}</div></div><el-empty v-if="!rows.length && !loading" description="暂无消息"/><el-button v-if="hasMore" text type="primary" @click="load">加载更多</el-button></div><el-dialog v-model="detailVisible" :title="detail?.title" width="560px"><div v-if="detail"><p>{{ detail.content }}</p><el-descriptions :column="2" border><el-descriptions-item label="类型">{{ detail.type }}</el-descriptions-item><el-descriptions-item label="优先级">{{ detail.priority }}</el-descriptions-item><el-descriptions-item label="状态">{{ detail.status }}</el-descriptions-item><el-descriptions-item label="时间">{{ detail.createTime }}</el-descriptions-item></el-descriptions></div></el-dialog></section></template>
<style scoped>.inbox-bar{display:flex;align-items:center;gap:12px;margin-bottom:12px}.msg-card{border-bottom:1px solid #e5eaec;padding:14px 0;cursor:pointer}.msg-title{font-weight:700;color:var(--hs-deep)}.msg-type{font-size:12px;color:var(--hs-muted);margin-top:4px}</style>