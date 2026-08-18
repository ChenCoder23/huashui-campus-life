<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'

const roles = ref<any[]>([])
const menus = ref<any[]>([])
const dialogVisible = ref(false)
const editRole = ref<any>(null)
const roleForm = reactive<any>({})
const menuForm = reactive<any>({ roleId: null, menuIds: [] })
const menuDialogVisible = ref(false)

async function load() {
  const [roleRes, menuRes] = await Promise.all([authApi.roles(), authApi.menus()])
  roles.value = roleRes.data || []
  menus.value = menuRes.data || []
}

function openEdit(row: any) {
  editRole.value = row
  Object.assign(roleForm, { roleName: row.roleName, description: row.description, sortOrder: row.sortOrder, status: row.status })
  dialogVisible.value = true
}

async function submitRole() {
  try {
    await authApi.updateRole(editRole.value.id, { ...roleForm })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } catch {}
}

function openMenus(row: any) {
  menuForm.roleId = row.id
  menuForm.menuIds = []
  dialogVisible.value = false
  menuDialogVisible.value = true
  loadRoleMenus(row.id)
}

async function loadRoleMenus(id: number) {
  const res: any = await authApi.roleMenus(id)
  const ids: number[] = []
  const walk = (list: any[]) => list.forEach((m) => { ids.push(m.id); if (m.children) walk(m.children) })
  walk(res.data || [])
  menuForm.menuIds = ids
}

async function submitMenus() {
  try {
    await authApi.setRoleMenus(menuForm.roleId, menuForm.menuIds)
    ElMessage.success('权限已保存')
  } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title">
      <div class="cn">角色管理</div><div class="en">Roles</div><div class="hs-waterline"></div>
    </div>
    <div class="hs-panel">
      <el-table :data="roles" border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="roleName" label="角色名" />
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80"><template #default="{ row }">{{ row.status === 1 || row.status === 'ENABLED' ? '启用' : '停用' }}</template></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="openMenus(row)">分配权限</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="编辑角色" width="480px" destroy-on-close>
      <el-form :model="roleForm" label-width="90px">
        <el-form-item label="角色名"><el-input v-model="roleForm.roleName" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="roleForm.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="roleForm.sortOrder" /></el-form-item>
        <el-form-item label="启用"><el-switch v-model="roleForm.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submitRole">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="menuDialogVisible" title="分配菜单权限" width="520px" destroy-on-close @closed="menuForm.roleId = null">
      <el-tree ref="treeRef" :data="menus" show-checkbox node-key="id" :props="{ label: 'menuName', children: 'children' }" :default-checked-keys="menuForm.menuIds" />
      <template #footer><el-button @click="menuDialogVisible=false">取消</el-button><el-button type="primary" @click="submitMenus">保存</el-button></template>
    </el-dialog>
  </section>
</template>