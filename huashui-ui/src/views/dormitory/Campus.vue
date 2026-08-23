<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { dormitoryApi } from '@/api'

const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const query = reactive<any>({ page: 1, size: 10 })
const dialogVisible = ref(false)
const editId = ref<number | null>(null)
const form = reactive<any>({ campusName: '', campusCode: '', address: '', sortOrder: 0 })

async function load() {
  loading.value = true
  try {
    const raw: any = await dormitoryApi.campusPage({ page: query.page, size: query.size })
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

function resetForm() {
  Object.assign(form, { campusName: '', campusCode: '', address: '', sortOrder: 0 })
}

function openCreate() {
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: any) {
  editId.value = row.id
  Object.assign(form, {
    campusName: row.campusName,
    campusCode: row.campusCode,
    address: row.address,
    sortOrder: row.sortOrder ?? 0
  })
  dialogVisible.value = true
}

async function submit() {
  if (!form.campusName || !form.campusCode) {
    ElMessage.warning('请填写校区名称和编码')
    return
  }
  try {
    if (editId.value === null) await dormitoryApi.createCampus({ ...form })
    else await dormitoryApi.updateCampus(editId.value, { ...form })
    ElMessage.success(editId.value === null ? '新增成功' : '保存成功')
    dialogVisible.value = false
    load()
  } catch {}
}

async function toggleStatus(row: any) {
  const isEnabled = row.status === 'ENABLED' || row.status === 1
  const nextStatus = isEnabled ? 'DISABLED' : 'ENABLED'
  try {
    await dormitoryApi.updateCampus(row.id, {
      campusName: row.campusName,
      campusCode: row.campusCode,
      address: row.address,
      sortOrder: row.sortOrder,
      status: nextStatus
    })
    ElMessage.success(nextStatus === 'ENABLED' ? '已启用' : '已禁用')
    load()
  } catch {}
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该校区吗？', '提示', { type: 'warning' })
  try {
    await dormitoryApi.deleteCampus(row.id)
    ElMessage.success('删除成功')
    load()
  } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">校区管理</div><div class="en">Campuses</div><div class="hs-waterline"></div></div>

    <div class="hs-panel">
      <div class="toolbar">
        <el-button type="primary" @click="openCreate">新增校区</el-button>
      </div>

      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="campusName" label="校区名称" />
        <el-table-column prop="campusCode" label="校区编码" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ENABLED' || row.status === 1 ? 'success' : 'info'">
              {{ row.status === 'ENABLED' || row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link :type="row.status === 'ENABLED' || row.status === 1 ? 'warning' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 'ENABLED' || row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          layout="total, sizes, prev, pager, next"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="load"
          @size-change="load"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId === null ? '新增校区' : '编辑校区'" width="520px" append-to-body destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="校区名称" required><el-input v-model="form.campusName" /></el-form-item>
        <el-form-item label="校区编码" required><el-input v-model="form.campusCode" /></el-form-item>
        <el-form-item label="校区地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<style scoped>
.toolbar { display: flex; justify-content: flex-start; margin-bottom: 14px; }
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>