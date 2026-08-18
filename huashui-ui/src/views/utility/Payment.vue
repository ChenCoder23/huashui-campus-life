<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { utilityApi } from '@/api'
const rows=ref<any[]>([]); const total=ref(0); const loading=ref(false)
const query=reactive({pageNum:1,pageSize:10,roomId:''})
async function load(){loading.value=true;try{const raw:any=await utilityApi.paymentPage({...query});const body=raw?.data??raw;rows.value=body?.records??[];total.value=Number(body?.total??rows.value.length)}finally{loading.value=false}}
onMounted(load)
</script>
<template><section class="hs-page"><div class="hs-page-title"><div class="cn">缴费记录</div><div class="en">Payments</div><div class="hs-waterline"></div></div><div class="hs-panel"><el-form inline @submit.prevent><el-form-item label="房间ID"><el-input v-model="query.roomId" clearable style="width:160px"/></el-form-item><el-button type="primary" @click="load">查询</el-button></el-form><el-table :data="rows" border stripe v-loading="loading"><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="orderNo" label="订单号"/><el-table-column prop="roomId" label="房间ID"/><el-table-column prop="paymentType" label="缴费类型"/><el-table-column prop="amount" label="金额"/><el-table-column prop="payMethod" label="支付方式"/><el-table-column prop="status" label="状态"/><el-table-column prop="paidTime" label="支付时间"/></el-table><div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load"/></div></div></section></template>
<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>