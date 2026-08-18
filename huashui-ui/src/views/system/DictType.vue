<script setup lang="ts">
import SchemaCrud from '@/components/SchemaCrud.vue'
import { systemApi } from '@/api'

const api = {
  page: systemApi.dictTypePage,
  create: systemApi.createDictType,
  update: systemApi.updateDictType,
  remove: systemApi.deleteDictType
}

const columns = [
  { prop: 'id', label: 'ID', width: 70 },
  { prop: 'dictName', label: '字典名称' },
  { prop: 'dictType', label: '字典类型' },
  { prop: 'description', label: '描述' },
  { prop: 'sortOrder', label: '排序', width: 80 },
  { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用') }
]

const search = [{ prop: 'keyword', label: '关键词', placeholder: '字典名称/类型' }]
const form = [
  { prop: 'dictName', label: '字典名称', required: true },
  { prop: 'dictType', label: '字典类型', required: true },
  { prop: 'description', label: '描述', type: 'textarea', span: 24 },
  { prop: 'sortOrder', label: '排序', type: 'number' },
  { prop: 'status', label: '启用', type: 'switch' }
]
</script>

<template>
  <SchemaCrud title="字典类型" en="Dictionary Types" :api="api" :columns="columns" :search="search" :form="form" />
</template>