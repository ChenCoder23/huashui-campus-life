<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { attendanceApi } from '@/api'

const rows = ref<any[]>([]); const total = ref(0); const loading = ref(false)
const stats = ref<any>(null)
const query = reactive({ pageNum:1, pageSize:10, workerId:'', campusId:'', buildingId:'', status:'', startDate:'', endDate:'' })
const updateVisible = ref(false)
const updateForm = reactive({ workerId:'', status:'NORMAL', remark:'' })

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => { if (params[k] === '' || params[k] == null) delete params[k] })
    const raw: any = await attendanceApi.adminPage(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
    const statsRes: any = await attendanceApi.statistics(params)
    stats.value = statsRes.data
  } finally { loading.value = false }
}

async function submitUpdate() {
  try { await attendanceApi.adminUpdate(updateForm); ElMessage.success('已更新'); updateVisible.value = false; load() } catch {}
}

function openUpdate(row: any) {
  Object.assign(updateForm, { workerId: row.workerId, status: row.checkInStatus || 'NORMAL', remark: row.remark || '' })
  updateVisible.value = true
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">考勤管理</div><div class="en">Attendance Admin</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="员工ID"><el-input v-model="query.workerId" clearable style="width:120px" /></el-form-item>
        <el-form-item label="校区ID"><el-input v-model="query.campusId" clearable style="width:120px" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="query.status" clearable style="width:120px"><el-option v-for="s in ['NORMAL','LATE','ABSENT','LEAVE','MAKEUP']" :key="s" :label="s" :value="s" /></el-select></el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="workerId" label="员工ID" />
        <el-table-column prop="workerName" label="姓名" />
        <el-table-column prop="attendanceDate" label="日期" />
        <el-table-column prop="checkInTime" label="打卡时间" />
        <el-table-column prop="checkInType" label="方式" />
        <el-table-column prop="checkInStatus" label="状态" />
        <el-table-column label="操作" width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="openUpdate(row)">修正</el-button></template></el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>
    <div class="hs-panel">
      <h3>统计</h3>
      <el-descriptions :column="5" border v-if="stats">
        <el-descriptions-item label="总数">{{ stats.total }}</el-descriptions-item>
        <el-descriptions-item label="正常">{{ stats.normal }}</el-descriptions-item>
        <el-descriptions-item label="迟到">{{ stats.late }}</el-descriptions-item>
        <el-descriptions-item label="缺勤">{{ stats.absent }}</el-descriptions-item>
        <el-descriptions-item label="请假">{{ stats.leave }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <el-dialog v-model="updateVisible" title="考勤修正" width="420px"><el-form :model="updateForm" label-width="80px"><el-form-item label="员工ID"><el-input v-model="updateForm.workerId" /></el-form-item><el-form-item label="状态"><el-select v-model="updateForm.status"><el-option v-for="s in ['NORMAL','LATE','ABSENT','LEAVE','MAKEUP']" :key="s" :label="s" :value="s" /></el-select></el-form-item><el-form-item label="备注"><el-input v-model="updateForm.remark" type="textarea" /></el-form-item></el-form><template #footer><el-button @click="updateVisible=false">取消</el-button><el-button type="primary" @click="submitUpdate">保存</el-button></template></el-dialog>
  </section>
</template>

<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}h3{margin:0 0 16px;color:var(--hs-deep)}</style>