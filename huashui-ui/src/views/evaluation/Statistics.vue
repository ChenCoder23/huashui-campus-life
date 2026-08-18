<script setup lang="ts">
import { reactive, ref } from 'vue'
import { evaluationApi } from '@/api'

const questionnaireId=ref('')
const stats=ref<any>(null)
const questionStats=ref<any[]>([])
const loading=ref(false)

async function load(){if(!questionnaireId.value)return;loading.value=true;try{const [a,b]:any[]=await Promise.all([evaluationApi.statistics(Number(questionnaireId.value)),evaluationApi.questionStats(Number(questionnaireId.value))]);stats.value=a.data;questionStats.value=b.data||[]}finally{loading.value=false}}
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">评价统计</div><div class="en">Evaluation Statistics</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-form inline @submit.prevent><el-form-item label="问卷ID"><el-input v-model="questionnaireId" style="width:160px"/></el-form-item><el-button type="primary" @click="load">查询</el-button></el-form><div v-if="stats" v-loading="loading"><el-descriptions :column="4" border><el-descriptions-item label="总人数">{{ stats.totalCount }}</el-descriptions-item><el-descriptions-item label="已提交">{{ stats.submitCount }}</el-descriptions-item></el-descriptions></div><el-table v-if="questionStats.length" :data="questionStats" border style="margin-top:16px"><el-table-column prop="title" label="题目"/><el-table-column prop="averageScore" label="平均分"/><el-table-column prop="submitCount" label="提交数"/></el-table></div></section></template>