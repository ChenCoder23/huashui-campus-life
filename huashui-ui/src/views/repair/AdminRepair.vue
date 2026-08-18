<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { repairApi } from '@/api'

const rows=ref<any[]>([]); const total=ref(0); const loading=ref(false)
const query=reactive({pageNum:1,pageSize:10,status:'',repairType:'',startDate:'',endDate:''})
const assignVisible=ref(false)
const assignForm=reactive({repairId:'',repairerId:'',repairerName:''})

async function load(){loading.value=true;try{const params={...query};Object.keys(params).forEach(k=>{if(params[k]===''||params[k]==null)delete params[k]});const raw:any=await repairApi.adminPage(params);const body=raw?.data??raw;rows.value=body?.records??[];total.value=Number(body?.total??rows.value.length)}finally{loading.value=false}}
function openAssign(row:any){assignForm.repairId=row.id;assignForm.repairerId='';assignForm.repairerName='';assignVisible.value=true}
async function submitAssign(){try{await repairApi.assign(assignForm);ElMessage.success('派单成功');assignVisible.value=false;load()}catch{}}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">报修管理</div><div class="en">Repair Admin</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-form inline @submit.prevent><el-form-item label="状态"><el-select v-model="query.status" clearable style="width:140px"><el-option v-for="s in ['PENDING','ASSIGNED','REPAIRING','COMPLETED','CANCELLED']" :key="s" :label="s" :value="s"/></el-select></el-form-item><el-form-item label="类型"><el-input v-model="query.repairType" clearable style="width:140px"/></el-form-item><el-button type="primary" @click="load">查询</el-button></el-form><el-table :data="rows" border stripe v-loading="loading"><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="orderNo" label="工单号"/><el-table-column prop="repairType" label="类型"/><el-table-column prop="status" label="状态"/><el-table-column prop="repairerName" label="维修工"/><el-table-column prop="createTime" label="提交时间"/><el-table-column label="操作" width="100" fixed="right"><template #default="{ row }"><el-button v-if="row.status==='PENDING'" link type="primary" @click="openAssign(row)">派单</el-button></template></el-table-column></el-table><div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load"/></div></div><el-dialog v-model="assignVisible" title="派单" width="420px"><el-form :model="assignForm" label-width="80px"><el-form-item label="报修ID"><el-input v-model="assignForm.repairId"/></el-form-item><el-form-item label="维修工ID"><el-input v-model="assignForm.repairerId"/></el-form-item><el-form-item label="维修工姓名"><el-input v-model="assignForm.repairerName"/></el-form-item></el-form><template #footer><el-button @click="assignVisible=false">取消</el-button><el-button type="primary" @click="submitAssign">保存</el-button></template></el-dialog></section></template>
<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>