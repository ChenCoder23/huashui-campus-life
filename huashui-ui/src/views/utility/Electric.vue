<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { utilityApi } from '@/api'
const rows=ref<any[]>([]); const total=ref(0); const loading=ref(false)
const query=reactive({pageNum:1,pageSize:10,buildingId:''})
async function load(){loading.value=true;try{const raw:any=await utilityApi.electricPage({...query});const body=raw?.data??raw;rows.value=body?.records??[];total.value=Number(body?.total??rows.value.length)}finally{loading.value=false}}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">电费余额</div><div class="en">Electricity</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-form inline @submit.prevent><el-form-item label="楼栋ID"><el-input v-model="query.buildingId" clearable style="width:160px"/></el-form-item><el-button type="primary" @click="load">查询</el-button></el-form><el-table :data="rows" border stripe v-loading="loading"><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="roomId" label="房间ID"/><el-table-column prop="balance" label="余额"/><el-table-column prop="freeQuota" label="免费额度"/><el-table-column prop="totalUsage" label="总用量"/><el-table-column prop="status" label="状态"/><el-table-column prop="updateTime" label="更新时间"/></el-table><div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load"/></div></div></section></template>
<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>