<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { storageApi } from '@/api'

const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const query = reactive<any>({ pageNum: 1, pageSize: 10, originalName: '', bizType: '', uploaderId: '', status: '' })

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => { if (params[k] === '' || params[k] === null || params[k] === undefined) delete params[k] })
    const raw: any = await storageApi.page(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该文件吗？', '提示', { type: 'warning' })
  try {
    await storageApi.remove(row.fileId || row.id)
    ElMessage.success('删除成功')
    load()
  } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">文件中心</div><div class="en">File Center</div><div class="hs-waterline"></div></div>

    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="文件名"><el-input v-model="query.originalName" clearable style="width:180px" /></el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="query.bizType" clearable style="width:150px">
            <el-option label="头像" value="AVATAR" />
            <el-option label="请假证明" value="LEAVE_PROOF" />
            <el-option label="报修图片" value="REPAIR_IMAGE" />
            <el-option label="考勤图片" value="ATTENDANCE" />
            <el-option label="保洁任务" value="CLEAN_TASK" />
            <el-option label="工作日志" value="WORK_LOG" />
            <el-option label="公告附件" value="NOTICE" />
          </el-select>
        </el-form-item>
        <el-form-item label="上传者ID"><el-input v-model="query.uploaderId" clearable style="width:120px" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable style="width:120px">
            <el-option label="正常" :value="1" />
            <el-option label="已删除" :value="0" />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form>

      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column label="预览" width="80">
          <template #default="{ row }">
            <el-image :src="row.url" fit="cover" style="width:40px;height:40px;border-radius:6px" />
          </template>
        </el-table-column>
        <el-table-column prop="originalName" label="文件名" show-overflow-tooltip />
        <el-table-column prop="bizType" label="业务类型" width="110" />
        <el-table-column prop="fileExt" label="扩展名" width="90" />
        <el-table-column prop="fileSize" label="大小" width="110">
          <template #default="{ row }">{{ row.fileSize ? (row.fileSize / 1024).toFixed(1) + ' KB' : '—' }}</template>
        </el-table-column>
        <el-table-column prop="uploaderId" label="上传者ID" width="100" />
        <el-table-column prop="createTime" label="上传时间" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, sizes, prev, pager, next" @current-change="load" @size-change="load" />
      </div>
    </div>
  </section>
</template>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>