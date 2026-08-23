<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { dormitoryApi } from '@/api'

const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const campuses = ref<any[]>([])
const buildings = ref<any[]>([])
const dialogVisible = ref(false)
const editId = ref<number | null>(null)

const query = reactive<any>({
  pageNum: 1,
  pageSize: 10,
  campusId: '',
  buildingId: '',
  roomNumber: '',
  floorNumber: '',
  status: ''
})

const form = reactive<any>({
  buildingId: '',
  roomNumber: '',
  floorNumber: 1,
  roomType: 'FOUR',
  totalBeds: 4,
  remark: ''
})

async function loadCampuses() {
  const res: any = await dormitoryApi.campusOptions()
  campuses.value = res.data || []
}

async function loadBuildings(campusId?: number) {
  const res: any = await dormitoryApi.buildingOptions(campusId)
  buildings.value = res.data || []
}

function handleCampusChange() {
  query.buildingId = ''
  loadBuildings(query.campusId ? Number(query.campusId) : undefined)
}

async function load() {
  loading.value = true
  try {
    const params = { ...query }
    Object.keys(params).forEach((k) => {
      if (params[k] === '' || params[k] === null || params[k] === undefined) delete params[k]
    })
    const raw: any = await dormitoryApi.roomPage(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

function resetForm() {
  Object.assign(form, { buildingId: '', roomNumber: '', floorNumber: 1, roomType: 'FOUR', totalBeds: 4, remark: '' })
}

function openCreate() {
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: any) {
  editId.value = row.id
  Object.assign(form, {
    buildingId: row.buildingId,
    roomNumber: row.roomNumber,
    floorNumber: row.floorNumber,
    roomType: row.roomType || 'FOUR',
    totalBeds: row.totalBeds,
    remark: row.remark || ''
  })
  dialogVisible.value = true
}

async function submit() {
  if (!form.buildingId || !form.roomNumber) {
    ElMessage.warning('请填写楼栋和房间号')
    return
  }
  try {
    if (editId.value === null) await dormitoryApi.createRoom({ ...form })
    else await dormitoryApi.updateRoom(editId.value, { ...form })
    ElMessage.success(editId.value === null ? '新增成功' : '保存成功')
    dialogVisible.value = false
    load()
  } catch {}
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该房间吗？', '提示', { type: 'warning' })
  try {
    await dormitoryApi.deleteRoom(row.id)
    ElMessage.success('删除成功')
    load()
  } catch {}
}

function getRoomTypeName(roomType: string) {
  const map: Record<string, string> = {
    FOUR: '四人间',
    SIX: '六人间'
  }

  return map[roomType] || roomType
}

function getRoomStatusName(status: string) {
  const map: Record<string, string> = {
    NORMAL: '正常',
    FULL: '住满',
    EMPTY: '空房',
    LOCKED: '封闭'
  }

  return map[status] || status
}

onMounted(() => {
  loadCampuses()
  loadBuildings()
  load()
})



</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">房间管理</div><div class="en">Rooms</div><div class="hs-waterline"></div></div>

    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="校区">
          <el-select v-model="query.campusId" clearable style="width:160px" @change="handleCampusChange">
            <el-option v-for="c in campuses" :key="c.id" :label="c.campusName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋">
          <el-select v-model="query.buildingId" clearable style="width:160px">
            <el-option v-for="b in buildings" :key="b.id" :label="b.buildingName" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号"><el-input v-model="query.roomNumber" clearable style="width:140px" /></el-form-item>
        <el-form-item label="楼层"><el-input v-model="query.floorNumber" clearable style="width:120px" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable style="width:140px">
            <el-option label="正常" value="NORMAL" />
            <el-option label="住满" value="FULL" />
            <el-option label="空房" value="EMPTY" />
            <el-option label="封闭" value="LOCKED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">查询</el-button>
          <el-button @click="query.buildingId=''; query.roomNumber=''; query.floorNumber=''; query.status=''; query.campusId=''; load()">重置</el-button>
          <el-button type="primary" @click="openCreate">新增房间</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="buildingId" label="楼栋ID" />
        <el-table-column prop="roomNumber" label="房间号" />
        <el-table-column prop="floorNumber" label="楼层" />
        <el-table-column label="房型">
          <template #default="{ row }">
            {{ getRoomTypeName(row.roomType) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalBeds" label="床位数" />
        <el-table-column prop="occupiedBeds" label="已住" />
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag
                :type="
        row.status === 'NORMAL' ? 'success' :
        row.status === 'EMPTY' ? '' :
        row.status === 'FULL' ? 'warning' :
        row.status === 'LOCKED' ? 'danger' :
        'info'
      "
            >
              {{ getRoomStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="load"
          @size-change="load"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId === null ? '新增房间' : '编辑房间'" width="560px" append-to-body destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="楼栋">
          <el-select v-model="form.buildingId" style="width:100%">
            <el-option v-for="b in buildings" :key="b.id" :label="b.buildingName" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号"><el-input v-model="form.roomNumber" /></el-form-item>
        <el-form-item label="楼层"><el-input-number v-model="form.floorNumber" style="width:100%" /></el-form-item>
        <el-form-item label="房型">
          <el-select v-model="form.roomType" style="width:100%">
            <el-option label="四人间" value="FOUR" />
            <el-option label="六人间" value="SIX" />
          </el-select>
        </el-form-item>
        <el-form-item label="床位数"><el-input-number v-model="form.totalBeds" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>