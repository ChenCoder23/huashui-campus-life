<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { dormitoryApi } from '@/api'

const rows = ref<any[]>([])
const campuses = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const detail = ref<any>(null)
const editId = ref<number | null>(null)
const query = reactive<any>({ pageNum: 1, pageSize: 10, campusId: '', buildingName: '', buildingCode: '' })
const total = ref(0)
const form = reactive<any>({})

const roomTypeMap: Record<string, string> = { FOUR: '四人间', SIX: '六人间' }
const bathTypeMap: Record<string, string> = { PRIVATE: '独立卫浴', PUBLIC_STALL: '公共隔间', DRY_WET_SEPARATED: '干湿分离' }
const balconyTypeMap: Record<string, string> = { STANDARD: '标准阳台', LARGE: '超大阳台' }
const hotWaterTypeMap: Record<string, string> = { ALL_DAY: '24小时供应', LIMITED: '限时供应' }
const bedTypeMap: Record<string, string> = { BUNK_DESK: '上床下桌', BUNK_MIXED: '上下铺混合' }

function mapText(map: Record<string, string>, value: any) {
  if (value === null || value === undefined || value === '') return '—'
  return map[value] || value
}

const boolFields = [
  'hasPrivateBath',
  'hasBalcony',
  'hasAc',
  'hasHeating',
  'hasDrinkingWater',
  'hasLaundry',
  'hasStudyRoom',
  'isStandardized'
]

const boolFieldLabels: Record<string, string> = {
  hasPrivateBath: '独立卫生间',
  hasBalcony: '独立阳台',
  hasAc: '空调',
  hasHeating: '暖气',
  hasDrinkingWater: '直饮水',
  hasLaundry: '洗衣设施',
  hasStudyRoom: '自习室',
  isStandardized: '标准化宿舍'
}

const textFields = ['campusId','area','buildingName','buildingCode','roomType','totalFloors','accommodationFee','description','sortOrder','bathType','balconyType','bedType','floorType','hotWaterType','hotWaterHours','bedSize']

function resetForm() {
  Object.keys(form).forEach((k) => delete form[k])
  boolFields.forEach((k) => (form[k] = false))
  textFields.forEach((k) => (form[k] = ''))
  form.totalFloors = 0
  form.accommodationFee = 0
  form.sortOrder = 0
  form.roomType = 'FOUR'
  form.bathType = 'PRIVATE'
  form.balconyType = 'STANDARD'
  form.hotWaterType = 'ALL_DAY'
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

async function openEdit(row: any) {
  editId.value = row.id
  resetForm()
  dialogVisible.value = true
  try {
    const res: any = await dormitoryApi.buildingDetail(row.id)
    const detail = res?.data ?? res
    Object.assign(form, detail, detail?.config || {})
  } catch {
    const campus = campuses.value.find((c) => c.campusName === row.campusName)
    Object.assign(form, row, { campusId: campus?.id || row.campusId || '' })
  }
}

async function openDetail(row: any) {
  detail.value = null
  detailVisible.value = true
  try {
    const res: any = await dormitoryApi.buildingDetail(row.id)
    detail.value = res?.data ?? res
  } catch {
    detailVisible.value = false
  }
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

async function toggleStatus(row: any) {
  const isEnabled = row.status === 'ENABLED' || row.status === 1
  const nextStatus = isEnabled ? 'DISABLED' : 'ENABLED'
  try {
    await dormitoryApi.updateBuilding(row.id, { ...row, status: nextStatus })
    ElMessage.success(nextStatus === 'ENABLED' ? '已启用' : '已禁用')
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
    <div class="hs-page-title"><div class="cn">楼栋管理</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="校区">
          <el-select v-model="query.campusId" clearable style="width:160px"><el-option v-for="c in campuses" :key="c.id" :label="c.campusName" :value="c.id" /></el-select>
        </el-form-item>
        <el-form-item label="楼栋名称"><el-input v-model="query.buildingName" clearable style="width:150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button type="primary" plain @click="openCreate">新增楼栋</el-button></el-form-item>
      </el-form>
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="编号" width="80" />
        <el-table-column prop="campusName" label="校区" />
        <el-table-column prop="area" label="区域" />
        <el-table-column prop="buildingName" label="楼栋名称" />
        <el-table-column prop="buildingCode" label="楼栋编码" />
        <el-table-column label="房型" width="100">
          <template #default="{ row }">{{ mapText(roomTypeMap, row.roomType) }}</template>
        </el-table-column>
        <el-table-column prop="totalFloors" label="总楼层" width="90" />
        <el-table-column prop="accommodationFee" label="住宿费" width="100" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ENABLED' || row.status === 1 ? 'success' : 'info'">
              {{ row.status === 'ENABLED' || row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="270" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">查看</el-button>
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link :type="row.status === 'ENABLED' || row.status === 1 ? 'warning' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 'ENABLED' || row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" /></div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId===null?'新增楼栋':'编辑楼栋'" width="720px" append-to-body destroy-on-close>
      <el-form :model="form" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="所属校区"><el-select v-model="form.campusId" style="width:100%"><el-option v-for="c in campuses" :key="c.id" :label="c.campusName" :value="c.id" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="所在区域"><el-input v-model="form.area" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="楼栋名称"><el-input v-model="form.buildingName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="楼栋编码"><el-input v-model="form.buildingCode" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="房型">
              <el-select v-model="form.roomType" style="width:100%">
                <el-option v-for="(label, value) in roomTypeMap" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="总楼层"><el-input-number v-model="form.totalFloors" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="住宿费"><el-input-number v-model="form.accommodationFee" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="排序"><el-input-number v-model="form.sortOrder" style="width:100%" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="楼栋简介"><el-input v-model="form.description" type="textarea" /></el-form-item></el-col>
        </el-row>
        <el-divider content-position="left">硬件配置</el-divider>
        <el-row :gutter="16">
          <el-col v-for="f in boolFields" :key="f" :span="12">
            <el-form-item :label="boolFieldLabels[f]">
              <el-switch v-model="form[f]" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卫浴类型">
              <el-select v-model="form.bathType" style="width:100%">
                <el-option v-for="(label, value) in bathTypeMap" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阳台类型">
              <el-select v-model="form.balconyType" style="width:100%">
                <el-option v-for="(label, value) in balconyTypeMap" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="床铺类型">
              <el-select v-model="form.bedType" style="width:100%">
                <el-option v-for="(label, value) in bedTypeMap" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="地面材质"><el-input v-model="form.floorType" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="热水供应">
              <el-select v-model="form.hotWaterType" style="width:100%">
                <el-option v-for="(label, value) in hotWaterTypeMap" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="供水时段"><el-input v-model="form.hotWaterHours" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="床铺尺寸"><el-input v-model="form.bedSize" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="楼栋详细信息" width="760px" append-to-body destroy-on-close>
      <template v-if="detail">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="楼栋编号">{{ detail.id }}</el-descriptions-item>
          <el-descriptions-item label="楼栋名称">{{ detail.buildingName }}</el-descriptions-item>
          <el-descriptions-item label="楼栋编码">{{ detail.buildingCode }}</el-descriptions-item>
          <el-descriptions-item label="所属校区">{{ detail.campusName || detail.campusId || '—' }}</el-descriptions-item>
          <el-descriptions-item label="所在区域">{{ detail.area || '—' }}</el-descriptions-item>
          <el-descriptions-item label="房型">{{ mapText(roomTypeMap, detail.roomType) }}</el-descriptions-item>
          <el-descriptions-item label="总楼层">{{ detail.totalFloors }}</el-descriptions-item>
          <el-descriptions-item label="住宿费">{{ detail.accommodationFee }} 元/年</el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.status === 'ENABLED' || detail.status === 1 ? '启用' : '停用' }}</el-descriptions-item>
          <el-descriptions-item label="楼栋简介" :span="3">{{ detail.description || '—' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">硬件配置</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="独立卫生间">{{ detail.config?.hasPrivateBath ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="卫浴类型">{{ mapText(bathTypeMap, detail.config?.bathType) }}</el-descriptions-item>
          <el-descriptions-item label="独立阳台">{{ detail.config?.hasBalcony ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="阳台类型">{{ mapText(balconyTypeMap, detail.config?.balconyType) }}</el-descriptions-item>
          <el-descriptions-item label="床铺类型">{{ mapText(bedTypeMap, detail.config?.bedType) }}</el-descriptions-item>
          <el-descriptions-item label="地面材质">{{ detail.config?.floorType || '—' }}</el-descriptions-item>
          <el-descriptions-item label="热水供应">{{ mapText(hotWaterTypeMap, detail.config?.hotWaterType) }}</el-descriptions-item>
          <el-descriptions-item label="供水时段">{{ detail.config?.hotWaterHours || '—' }}</el-descriptions-item>
          <el-descriptions-item label="空调">{{ detail.config?.hasAc ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="暖气">{{ detail.config?.hasHeating ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="直饮水">{{ detail.config?.hasDrinkingWater ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="洗衣设施">{{ detail.config?.hasLaundry ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="公共自习室">{{ detail.config?.hasStudyRoom ? '有' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="床铺尺寸">{{ detail.config?.bedSize || '—' }}</el-descriptions-item>
        </el-descriptions>
      </template>
      <template #footer><el-button type="primary" @click="detailVisible=false">关闭</el-button></template>
    </el-dialog>
  </section>
</template>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>