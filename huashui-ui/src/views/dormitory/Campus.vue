<script setup lang="ts">
import SchemaCrud from '@/components/SchemaCrud.vue'
import { dormitoryApi } from '@/api'

const api = {
  page: (params: any) => dormitoryApi.campusPage({ page: params.pageNum, size: params.pageSize }),
  create: dormitoryApi.createCampus,
  update: dormitoryApi.updateCampus,
  remove: dormitoryApi.deleteCampus
}

const columns = [
  { prop: 'id', label: 'ID', width: 70 },
  { prop: 'campusName', label: '校区名称' },
  { prop: 'campusCode', label: '校区编码' },
  { prop: 'address', label: '地址' },
  { prop: 'sortOrder', label: '排序', width: 80 },
  { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用') }
]

const search: any[] = []
const form = [
  { prop: 'campusName', label: '校区名称', required: true },
  { prop: 'campusCode', label: '校区编码', required: true },
  { prop: 'address', label: '地址' },
  { prop: 'sortOrder', label: '排序', type: 'number' },
  { prop: 'status', label: '启用', type: 'switch' }
]
</script>
<template><SchemaCrud title="校区管理" en="Campuses" :api="api" :columns="columns" :search="search" :form="form" /></template>