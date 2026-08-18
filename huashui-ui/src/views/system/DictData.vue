<script setup lang="ts">
import SchemaCrud from '@/components/SchemaCrud.vue'
import { systemApi } from '@/api'

const api = {
  page: systemApi.dictDataPage,
  create: systemApi.createDictData,
  update: systemApi.updateDictData,
  remove: systemApi.deleteDictData
}

const columns = [
  { prop: 'id', label: 'ID', width: 70 },
  { prop: 'dictType', label: '字典类型' },
  { prop: 'dictLabel', label: '字典标签' },
  { prop: 'dictValue', label: '字典值' },
  { prop: 'sortOrder', label: '排序', width: 80 },
  { prop: 'isDefault', label: '默认', width: 80, formatter: (row) => (row.isDefault ? '是' : '否') },
  { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用') }
]

const search = [{ prop: 'dictType', label: '字典类型' }]
const form = [
  { prop: 'dictType', label: '字典类型', required: true },
  { prop: 'dictLabel', label: '字典标签', required: true },
  { prop: 'dictValue', label: '字典值', required: true },
  { prop: 'sortOrder', label: '排序', type: 'number' },
  { prop: 'isDefault', label: '默认', type: 'switch' },
  { prop: 'remark', label: '备注', type: 'textarea', span: 24 },
  { prop: 'status', label: '启用', type: 'switch' }
]
</script>

<template>
  <SchemaCrud title="字典数据" en="Dictionary Data" :api="api" :columns="columns" :search="search" :form="form" />
</template>