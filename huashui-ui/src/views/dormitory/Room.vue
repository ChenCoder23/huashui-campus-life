<script setup lang="ts">
import SchemaCrud from '@/components/SchemaCrud.vue'
import { dormitoryApi } from '@/api'

const api = { page: dormitoryApi.roomPage, create: dormitoryApi.createRoom, update: dormitoryApi.updateRoom, remove: dormitoryApi.deleteRoom }
const columns = [
  { prop: 'id', label: 'ID', width: 70 },
  { prop: 'buildingId', label: '楼栋ID' },
  { prop: 'roomNumber', label: '房间号' },
  { prop: 'floorNumber', label: '楼层' },
  { prop: 'roomType', label: '房型' },
  { prop: 'totalBeds', label: '床位数' },
  { prop: 'occupiedBeds', label: '已住' },
  { prop: 'status', label: '状态' }
]
const search = [
  { prop: 'buildingId', label: '楼栋ID' },
  { prop: 'floorNumber', label: '楼层', type: 'number' },
  { prop: 'status', label: '状态' }
]
const form = [
  { prop: 'buildingId', label: '楼栋ID', required: true, type: 'number' },
  { prop: 'roomNumber', label: '房间号', required: true },
  { prop: 'floorNumber', label: '楼层', type: 'number' },
  { prop: 'roomType', label: '房型', type: 'select', options: [{ label: '四人间', value: 'FOUR' }, { label: '六人间', value: 'SIX' }] },
  { prop: 'totalBeds', label: '床位数', type: 'number' },
  { prop: 'remark', label: '备注', type: 'textarea', span: 24 }
]
</script>
<template><SchemaCrud title="房间管理" en="Rooms" :api="api" :columns="columns" :search="search" :form="form" /></template>