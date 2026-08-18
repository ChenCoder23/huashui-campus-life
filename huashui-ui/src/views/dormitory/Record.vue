<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { dormitoryApi } from '@/api'

const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const query = reactive<any>({ pageNum: 1, pageSize: 10, buildingId: '', studentId: '' })
const assignVisible = ref(false)
const assignForm = reactive({ studentId: '', roomId: '', bedId: '' })
const adjustVisible = ref(false)
const adjustForm = reactive({ studentId: '', newRoomId: '', newBedId: '' })

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => { if (params[k] === '' || params[k] == null) delete params[k] })
    const raw: any = await dormitoryApi.recordPage(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally { loading.value = false }
}

async function assign() {
  try { await dormitoryApi.assignRecord({ ...assignForm }); ElMessage.success('分配成功'); assignVisible.value = false; load() } catch {}
}

async function adjust() {
  try { await dormitoryApi.adjustRecord({ ...adjustForm }); ElMessage.success('调宿成功'); adjustVisible.value = false; load() } catch {}
}

async function checkout(row: any) {
  try { await dormitoryApi.checkoutRecord(row.studentId); ElMessage.success('退宿办理成功'); load() } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">住宿记录</div><div class="en">Accommodation Records</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="楼栋ID"><el-input v-model="query.buildingId" clearable style="width:150px" /></el-form-item>
        <el-form-item label="学生ID"><el-input v-model="query.studentId" clearable style="width:150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button type="primary" plain @click="assignVisible=true">分配床位</el-button><el-button type="primary" plain @click="adjustVisible=true">调宿</el-button></el-form-item>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="studentId" label="学生ID" />
        <el-table-column prop="campusId" label="校区ID" />
        <el-table-column prop="buildingId" label="楼栋ID" />
        <el-table-column prop="roomId" label="房间ID" />
        <el-table-column prop="bedId" label="床位ID" />
        <el-table-column prop="checkInTime" label="入住时间" />
        <el-table-column prop="checkOutTime" label="退宿时间" />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作" width="100" fixed="right"><template #default="{ row }"><el-button link type="danger" @click="checkout(row)">退宿</el-button></template></el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>

    <el-dialog v-model="assignVisible" title="分配床位" width="420px"><el-form :model="assignForm" label-width="80px"><el-form-item label="学生ID"><el-input v-model="assignForm.studentId" /></el-form-item><el-form-item label="房间ID"><el-input v-model="assignForm.roomId" /></el-form-item><el-form-item label="床位ID"><el-input v-model="assignForm.bedId" /></el-form-item></el-form><template #footer><el-button @click="assignVisible=false">取消</el-button><el-button type="primary" @click="assign">保存</el-button></template></el-dialog>
    <el-dialog v-model="adjustVisible" title="调宿" width="420px"><el-form :model="adjustForm" label-width="80px"><el-form-item label="学生ID"><el-input v-model="adjustForm.studentId" /></el-form-item><el-form-item label="新房间ID"><el-input v-model="adjustForm.newRoomId" /></el-form-item><el-form-item label="新床位ID"><el-input v-model="adjustForm.newBedId" /></el-form-item></el-form><template #footer><el-button @click="adjustVisible=false">取消</el-button><el-button type="primary" @click="adjust">保存</el-button></template></el-dialog>
  </section>
</template>

<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>