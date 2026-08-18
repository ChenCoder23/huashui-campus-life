<script setup lang="ts">
import SchemaCrud from '@/components/SchemaCrud.vue'
import { systemApi } from '@/api'

const api = {
  page: systemApi.configPage,
  create: systemApi.createConfig,
  update: systemApi.updateConfig,
  remove: systemApi.deleteConfig
}

const columns = [
  { prop: 'id', label: 'ID', width: 70 },
  { prop: 'configName', label: '配置名称' },
  { prop: 'configKey', label: '配置键' },
  { prop: 'configValue', label: '配置值' },
  { prop: 'configGroup', label: '分组' },
  { prop: 'sortOrder', label: '排序', width: 80 },
  { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用') }
]

const search = [{ prop: 'configGroup', label: '配置分组' }]
const form = [
  { prop: 'configName', label: '配置名称', required: true },
  { prop: 'configKey', label: '配置键', required: true },
  { prop: 'configValue', label: '配置值', required: true },
  { prop: 'configGroup', label: '配置分组' },
  { prop: 'description', label: '描述', type: 'textarea', span: 24 },
  { prop: 'sortOrder', label: '排序', type: 'number' },
  { prop: 'status', label: '启用', type: 'switch' }
]
</script>

<template>
  <SchemaCrud title="参数配置" en="System Config" :api="api" :columns="columns" :search="search" :form="form" />
</template>