<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { repairApi } from '@/api'

const rows=ref<any[]>([]); const total=ref(0); const loading=ref(false)
const query=reactive({pageNum:1,pageSize:10,status:''})
const completeVisible=ref(false)
const completeForm=reactive({repairId:'',repairResult:'',repairImages:[]})

async function load(){loading.value=true;try{const params={...query};Object.keys(params).forEach(k=>{if(params[k]===''||params[k]==null)delete params[k]});const raw:any=await repairApi.workerPage(params);const body=raw?.data??raw;rows.value=body?.records??[];total.value=Number(body?.total??rows.value.length)}finally{loading.value=false}}
async function start(row:any){try{await repairApi.workerStart(row.id);ElMessage.success('已开始维修');load()}catch{}}
function openComplete(row:any){completeForm.repairId=row.id;completeForm.repairResult='';completeForm.repairImages=[];completeVisible.value=true}
async function submitComplete(){try{await repairApi.workerComplete(completeForm);ElMessage.success('维修完成');completeVisible.value=false;load()}catch{}}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">我的维修</div><div class="en">Worker Repairs</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-form inline @submit.prevent><el-form-item label="状态"><el-select v-model="query.status" clearable style="width:140px"><el-option v-for="s in ['ASSIGNED','REPAIRING','COMPLETED']" :key="s" :label="s" :value="s"/></el-select></el-form-item><el-button type="primary" @click="load">查询</el-button></el-form><el-table :data="rows" border stripe v-loading="loading"><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="orderNo" label="工单号"/><el-table-column prop="repairType" label="类型"/><el-table-column prop="status" label="状态"/><el-table-column prop="createTime" label="提交时间"/><el-table-column label="操作" width="140" fixed="right"><template #default="{ row }"><el-button v-if="row.status==='ASSIGNED'" link type="primary" @click="start(row)">开始</el-button><el-button v-if="row.status==='REPAIRING'" link type="success" @click="openComplete(row)">完成</el-button></template></el-table-column></el-table><div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load"/></div></div><el-dialog v-model="completeVisible" title="完成维修" width="520px"><el-form :model="completeForm" label-width="90px"><el-form-item label="维修结果"><el-input v-model="completeForm.repairResult" type="textarea" :rows="4"/></el-form-item><el-form-item label="完工图片"><el-input v-model="completeForm.repairImages" placeholder="多个URL用逗号分隔" /></el-form-item></el-form><template #footer><el-button @click="completeVisible=false">取消</el-button><el-button type="primary" @click="submitComplete">完成</el-button></template></el-dialog></section></template>
<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>