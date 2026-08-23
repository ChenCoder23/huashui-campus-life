<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { authApi } from '@/api'

const rows = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editId = ref<number | null>(null)
const form = reactive<any>({})

async function load() {
  loading.value = true
  try {
    const res: any = await authApi.menus()
    rows.value = res.data || []
  } finally {
    loading.value = false
  }
}

function openCreate(parentId = 0) {
  editId.value = null
  Object.assign(form, { parentId, menuName: '', menuType: 'MENU', path: '', component: '', icon: '', permission: '', sortOrder: 0, isHome: 0, hidden: 0, status: 1 })
  dialogVisible.value = true
}

function openEdit(row: any) {
  editId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

async function submit() {
  try {
    if (editId.value === null) await authApi.createMenu({ ...form })
    else await authApi.updateMenu({ ...form, id: editId.value })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } catch {}
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该菜单吗？', '提示', { type: 'warning' })
  try {
    await authApi.deleteMenu(row.id)
    ElMessage.success('删除成功')
    load()
  } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title">
      <div class="cn">菜单管理</div><div class="en">Menus</div><div class="hs-waterline"></div>
    </div>
    <div class="hs-panel">
      <el-button type="primary" @click="openCreate(0)">新增根菜单</el-button>
      <el-table :data="rows" row-key="id" border v-loading="loading" style="margin-top: 14px">
        <el-table-column prop="menuName" label="菜单名称" min-width="160" />
        <el-table-column prop="menuType" label="类型" width="100" />
        <el-table-column prop="path" label="路由" min-width="140" />
        <el-table-column prop="component" label="组件" min-width="140" />
        <el-table-column prop="icon" label="图标" width="100" />
        <el-table-column prop="permission" label="权限标识" min-width="140" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">{{ row.status === 1 ? '启用' : '停用' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="openCreate(row.id)">添加子项</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId === null ? '新增菜单' : '编辑菜单'" width="620px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="菜单名称"><el-input v-model="form.menuName" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.menuType"><el-option label="目录" value="DIRECTORY" /><el-option label="菜单" value="MENU" /><el-option label="按钮" value="BUTTON" /></el-select>
        </el-form-item>
        <el-form-item label="路由"><el-input v-model="form.path" /></el-form-item>
        <el-form-item label="组件"><el-input v-model="form.component" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item>
        <el-form-item label="权限标识"><el-input v-model="form.permission" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" /></el-form-item>
        <el-form-item label="首页"><el-switch v-model="form.isHome" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="隐藏"><el-switch v-model="form.hidden" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="启用"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>