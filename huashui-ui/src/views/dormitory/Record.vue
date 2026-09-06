<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { dormitoryApi, systemApi } from '@/api'

const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const semesters = ref<any[]>([])
const campuses = ref<any[]>([])
const buildings = ref<any[]>([])
const query = reactive<any>({
  pageNum: 1,
  pageSize: 10,
  semester: '',
  campusName: '',
  buildingName: '',
  studentName: ''
})
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

async function loadSemesters() {
  const res: any = await systemApi.dictDataByType('semester')
  semesters.value = res?.data ?? res ?? []
}

async function loadCampuses() {
  const res: any = await dormitoryApi.campusOptions()
  campuses.value = res?.data ?? res ?? []
}

async function loadBuildings(campusId?: number) {
  const res: any = await dormitoryApi.buildingOptions(campusId)
  buildings.value = res?.data ?? res ?? []
}

function handleCampusChange() {
  query.buildingName = ''
  loadBuildings(query.campusName ? campuses.value.find((item) => item.campusName === query.campusName)?.id : undefined)
}

function semesterName(value: any) {
  if (!value) return '—'
  return semesters.value.find((item) => item.dictValue === value)?.dictLabel || value
}

function statusName(value: any) {
  const map: Record<string, string> = { '0': '已退宿', '1': '在住', LEFT: '已退宿', LIVING: '在住' }
  return map[String(value)] || value || '—'
}

async function reset() {
  Object.assign(query, { pageNum: 1, pageSize: 10, semester: '', campusName: '', buildingName: '', studentName: '' })
  await loadBuildings()
  await load()
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

onMounted(async () => {
  await Promise.all([loadSemesters(), loadCampuses(), loadBuildings()])
  await load()
})
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">住宿记录</div><div class="en">Accommodation Records</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="学期">
          <el-select v-model="query.semester" clearable filterable style="width:180px">
            <el-option v-for="item in semesters" :key="item.id" :label="item.dictLabel" :value="item.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="校区">
          <el-select v-model="query.campusName" clearable filterable style="width:160px" @change="handleCampusChange">
            <el-option v-for="item in campuses" :key="item.id" :label="item.campusName" :value="item.campusName" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋">
          <el-select v-model="query.buildingName" clearable filterable style="width:160px">
            <el-option v-for="item in buildings" :key="item.id" :label="item.buildingName" :value="item.buildingName" />
          </el-select>
        </el-form-item>
        <el-form-item label="学生姓名"><el-input v-model="query.studentName" clearable style="width:150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button @click="reset">重置</el-button><el-button type="primary" plain @click="assignVisible=true">分配床位</el-button><el-button type="primary" plain @click="adjustVisible=true">调宿</el-button></el-form-item>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="campusName" label="校区" />
        <el-table-column prop="buildingName" label="楼栋" />
        <el-table-column prop="roomNumber" label="房间号" />
        <el-table-column prop="bedNumber" label="床位号" />
        <el-table-column label="学期">
          <template #default="{ row }">{{ semesterName(row.semester) }}</template>
        </el-table-column>
        <el-table-column prop="checkInTime" label="入住时间" />
        <el-table-column prop="checkOutTime" label="退宿时间" />
        <el-table-column label="状态">
          <template #default="{ row }">{{ statusName(row.status) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right"><template #default="{ row }"><el-button link type="danger" @click="checkout(row)">退宿</el-button></template></el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>

    <el-dialog v-model="assignVisible" title="分配床位" width="420px"><el-form :model="assignForm" label-width="80px"><el-form-item label="学生ID"><el-input v-model="assignForm.studentId" /></el-form-item><el-form-item label="房间ID"><el-input v-model="assignForm.roomId" /></el-form-item><el-form-item label="床位ID"><el-input v-model="assignForm.bedId" /></el-form-item></el-form><template #footer><el-button @click="assignVisible=false">取消</el-button><el-button type="primary" @click="assign">保存</el-button></template></el-dialog>
    <el-dialog v-model="adjustVisible" title="调宿" width="420px"><el-form :model="adjustForm" label-width="80px"><el-form-item label="学生ID"><el-input v-model="adjustForm.studentId" /></el-form-item><el-form-item label="新房间ID"><el-input v-model="adjustForm.newRoomId" /></el-form-item><el-form-item label="新床位ID"><el-input v-model="adjustForm.newBedId" /></el-form-item></el-form><template #footer><el-button @click="adjustVisible=false">取消</el-button><el-button type="primary" @click="adjust">保存</el-button></template></el-dialog>
  </section>
</template>

<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>
