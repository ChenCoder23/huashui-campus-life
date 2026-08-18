<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { systemApi } from '@/api'
import { useRoute } from 'vue-router'

const route = useRoute()
const logType = computed(() => route.meta.logType as string)
const loading = ref(false)
const rows = ref<any[]>([])
const total = ref(0)
const query = reactive<any>({ pageNum: 1, pageSize: 10, keyword: '', module: '', status: '' })

const config = computed(() => {
  if (logType.value === 'login') {
    return {
      columns: [
        { prop: 'id', label: 'ID', width: 70 },
        { prop: 'username', label: '用户名' },
        { prop: 'loginTime', label: '登录时间' },
        { prop: 'ipAddress', label: 'IP' },
        { prop: 'device', label: '设备' },
        { prop: 'browser', label: '浏览器' },
        { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'SUCCESS' ? '成功' : '失败') }
      ],
      search: [{ prop: 'keyword', label: '用户名' }]
    }
  }
  if (logType.value === 'operation') {
    return {
      columns: [
        { prop: 'id', label: 'ID', width: 70 },
        { prop: 'operatorName', label: '操作人' },
        { prop: 'operationModule', label: '模块' },
        { prop: 'operationDesc', label: '操作' },
        { prop: 'requestUrl', label: '请求地址' },
        { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 || row.status === 'SUCCESS' ? '成功' : '失败') },
        { prop: 'createTime', label: '时间' }
      ],
      search: [{ prop: 'module', label: '模块' }]
    }
  }
  return {
    columns: [
      { prop: 'id', label: 'ID', width: 70 },
      { prop: 'exceptionName', label: '异常类型' },
      { prop: 'exceptionMsg', label: '异常信息' },
      { prop: 'serviceName', label: '服务' },
      { prop: 'requestUrl', label: '请求地址' },
      { prop: 'status', label: '状态', width: 90, formatter: (row) => (row.status === 1 ? '已处理' : '未处理') },
      { prop: 'createTime', label: '时间' }
    ],
    search: [{ prop: 'status', label: '状态' }]
  }
})

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => { if (params[k] === '' || params[k] == null) delete params[k] })
    const fn = logType.value === 'login' ? systemApi.loginLogs : logType.value === 'operation' ? systemApi.operationLogs : systemApi.exceptionLogs
    const raw: any = await fn(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title">
      <div class="cn">{{ route.meta.title }}</div>
      <div class="en">Logs</div>
      <div class="hs-waterline"></div>
    </div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item v-for="f in config.search" :key="f.prop" :label="f.label">
          <el-input v-model="query[f.prop]" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="rows" v-loading="loading" border stripe>
        <el-table-column v-for="col in config.columns" :key="col.prop" :prop="col.prop" :label="col.label" :width="col.width" show-overflow-tooltip>
          <template #default="{ row }">{{ col.formatter ? col.formatter(row) : row[col.prop] }}</template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" />
      </div>
    </div>
  </section>
</template>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>