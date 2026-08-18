<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { dormitoryApi } from '@/api'

const rows = ref<any[]>([])
const campuses = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editId = ref<number | null>(null)
const query = reactive<any>({ pageNum: 1, pageSize: 10, campusId: '', buildingName: '', buildingCode: '' })
const total = ref(0)
const form = reactive<any>({})

const boolFields = ['hasPrivateBath','hasBalcony','hasAc','hasHeating','hasDrinkingWater','hasLaundry','hasStudyRoom','isStandardized']
const textFields = ['campusId','area','buildingName','buildingCode','roomType','totalFloors','accommodationFee','description','sortOrder','bathType','balconyType','bedType','floorType','hotWaterType','hotWaterHours','bedSize']

function resetForm() {
  Object.keys(form).forEach((k) => delete form[k])
  boolFields.forEach((k) => (form[k] = false))
  textFields.forEach((k) => (form[k] = ''))
  form.totalFloors = 0
  form.accommodationFee = 0
  form.sortOrder = 0
}

async function loadCampus() {
  const res: any = await dormitoryApi.campusOptions()
  campuses.value = res.data || []
}

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => { if (params[k] === '' || params[k] == null) delete params[k] })
    const raw: any = await dormitoryApi.buildingPage(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: any) {
  editId.value = row.id
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}

async function submit() {
  try {
    if (editId.value === null) await dormitoryApi.createBuilding({ ...form })
    else await dormitoryApi.updateBuilding(editId.value, { ...form })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } catch {}
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该楼栋吗？', '提示', { type: 'warning' })
  try { await dormitoryApi.deleteBuilding(row.id); ElMessage.success('删除成功'); load() } catch {}
}

onMounted(() => { resetForm(); loadCampus(); load() })
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">楼栋管理</div><div class="en">Buildings</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="校区">
          <el-select v-model="query.campusId" clearable style="width:160px"><el-option v-for="c in campuses" :key="c.id" :label="c.campusName" :value="c.id" /></el-select>
        </el-form-item>
        <el-form-item label="名称"><el-input v-model="query.buildingName" clearable style="width:150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button type="primary" plain @click="openCreate">新增楼栋</el-button></el-form-item>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="campusName" label="校区" />
        <el-table-column prop="area" label="区域" />
        <el-table-column prop="buildingName" label="楼栋名称" />
        <el-table-column prop="buildingCode" label="编码" />
        <el-table-column prop="roomType" label="房型" />
        <el-table-column prop="totalFloors" label="楼层" width="70" />
        <el-table-column prop="accommodationFee" label="住宿费" />
        <el-table-column prop="status" label="状态" width="80"><template #default="{ row }">{{ row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用' }}</template></el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }"><el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId===null?'新增楼栋':'编辑楼栋'" width="720px" destroy-on-close>
      <el-form :model="form" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="校区"><el-select v-model="form.campusId" style="width:100%"><el-option v-for="c in campuses" :key="c.id" :label="c.campusName" :value="c.id" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="区域"><el-input v-model="form.area" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="楼栋名称"><el-input v-model="form.buildingName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="楼栋编码"><el-input v-model="form.buildingCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="房型"><el-select v-model="form.roomType" style="width:100%"><el-option label="四人间" value="FOUR" /><el-option label="六人间" value="SIX" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="总楼层"><el-input-number v-model="form.totalFloors" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="住宿费"><el-input-number v-model="form.accommodationFee" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="排序"><el-input-number v-model="form.sortOrder" style="width:100%" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">硬件配置</el-divider>
        <el-row :gutter="16">
          <el-col v-for="f in boolFields" :key="f" :span="12">
            <el-form-item :label="f"><el-switch v-model="form[f]" /></el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="卫浴类型"><el-input v-model="form.bathType" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="阳台类型"><el-input v-model="form.balconyType" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="床铺类型"><el-input v-model="form.bedType" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="地板类型"><el-input v-model="form.floorType" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="热水类型"><el-input v-model="form.hotWaterType" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="供水时段"><el-input v-model="form.hotWaterHours" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="床尺寸"><el-input v-model="form.bedSize" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<style scoped>.pager{display:flex;justify-content:flex-end;margin-top:16px}</style>