<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { leaveApi } from '@/api'
import { useAuthStore } from '@/store/auth'

const auth = useAuthStore()
const isManager = computed(() => ['SUPER_ADMIN', 'DORM_MANAGER'].includes(auth.profile?.userType || ''))
const isStaff = computed(() => ['STUDENT', 'CLEANER', 'REPAIRER'].includes(auth.profile?.userType || ''))
const rows = ref<any[]>([]); const total = ref(0); const loading = ref(false)
const query = reactive({ pageNum:1, pageSize:10, status:'' })
const createVisible = ref(false)
const form = reactive({ leaveType:'', startTime:'', endTime:'', reason:'', proofImages:'', campusId:'' })

async function load() {
  loading.value = true
  try {
    const params = { page: query.pageNum, size: query.pageSize }
    if (query.status) params.status = query.status
    const raw: any = await leaveApi.page(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally { loading.value = false }
}

async function submit() {
  try { await leaveApi.submit(form); ElMessage.success('请假申请已提交'); createVisible.value = false; load() } catch {}
}

async function approve(row: any) {
  try { await leaveApi.approve(row.id, '同意'); ElMessage.success('已通过'); load() } catch {}
}

async function reject(row: any) {
  try { await leaveApi.reject(row.id, '不同意'); ElMessage.success('已驳回'); load() } catch {}
}

async function cancel(row: any) {
  await ElMessageBox.confirm('确定撤回该请假申请吗？', '提示', { type: 'warning' })
  try { await leaveApi.cancel(row.id); ElMessage.success('已撤回'); load() } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">请假管理</div><div class="en">Leave Requests</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="状态"><el-select v-model="query.status" clearable style="width:140px"><el-option v-for="s in ['PENDING','APPROVED','REJECTED','CANCELLED']" :key="s" :label="s" :value="s" /></el-select></el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="primary" v-if="isStaff" @click="createVisible=true">申请请假</el-button>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="applicantName" label="申请人" />
        <el-table-column prop="leaveType" label="类型" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="reason" label="原因" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="approveOpinion" label="审批意见" />
        <el-table-column label="操作" width="190" fixed="right">
          <template #default="{ row }">
            <el-button v-if="isManager && row.status==='PENDING'" link type="primary" @click="approve(row)">通过</el-button>
            <el-button v-if="isManager && row.status==='PENDING'" link type="danger" @click="reject(row)">驳回</el-button>
            <el-button v-if="isStaff && ['PENDING','APPROVED'].includes(row.status)" link type="warning" @click="cancel(row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>

    <el-dialog v-if="isStaff" v-model="createVisible" title="申请请假" width="560px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="类型"><el-input v-model="form.leaveType" placeholder="如：事假 / 病假" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="校区ID"><el-input v-model="form.campusId" /></el-form-item>
        <el-form-item label="证明图片"><el-input v-model="form.proofImages" placeholder="多个URL用逗号分隔" /></el-form-item>
        <el-form-item label="原因"><el-input v-model="form.reason" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="createVisible=false">取消</el-button><el-button type="primary" @click="submit">提交</el-button></template>
    </el-dialog>
  </section>
</template>

<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>