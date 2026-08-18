<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { evaluationApi } from '@/api'

const rows=ref<any[]>([]); const detail=ref<any>(null); const loading=ref(false); const detailVisible=ref(false)
const answers=ref<any[]>([])

async function load(){loading.value=true;try{const res:any=await evaluationApi.myResponses();rows.value=res.data||[]}finally{loading.value=false}}
async function openDetail(row:any){const res:any=await evaluationApi.responseDetail(row.id);detail.value=res.data;answers.value=(res.data?.questions||[]).map((q:any)=>({questionId:q.id,score:0,content:''}));detailVisible.value=true}
async function submit(){try{await evaluationApi.submitResponse(detail.value.id,{answers:answers.value});ElMessage.success('提交成功');detailVisible.value=false;load()}catch{}}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">待我评价</div><div class="en">My Evaluations</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-table :data="rows" border stripe v-loading="loading"><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="title" label="问卷"/><el-table-column prop="description" label="描述"/><el-table-column prop="startTime" label="开始"/><el-table-column prop="endTime" label="截止"/><el-table-column prop="questionCount" label="题目数"/><el-table-column prop="status" label="状态"/><el-table-column label="操作" width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="openDetail(row)">去评价</el-button></template></el-table-column></el-table></div><el-dialog v-model="detailVisible" :title="detail?.title" width="640px"><div v-if="detail"><p>{{ detail.description }}</p><el-form label-width="100px"><el-form-item v-for="(q,index) in detail.questions" :key="q.id" :label="q.title"><el-input-number v-model="answers[index].score" :min="q.minScore" :max="q.maxScore" /><el-input v-model="answers[index].content" placeholder="建议" /></el-form-item></el-form></div><template #footer><el-button @click="detailVisible=false">取消</el-button><el-button type="primary" @click="submit">提交</el-button></template></el-dialog></section></template>