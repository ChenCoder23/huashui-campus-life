<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { authApi } from '@/api'

const activeRole = ref('DORM_MANAGER')
const rows = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, role: activeRole.value, keyword: '' })
const createVisible = ref(false)
const resetVisible = ref(false)
const currentUser = ref<any>(null)
const resetPassword = ref('')

const createForm = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  roleCode: activeRole.value,
  gender: 1,
  campusId: '',
  buildingId: ''
})

const roleMap: Record<string, string> = {
  DORM_MANAGER: '宿舍管理员',
  REPAIRER: '维修工',
  CLEANER: '清洁工'
}

async function load() {
  loading.value = true
  try {
    query.role = activeRole.value
    const raw: any = await authApi.adminUserPage({ ...query })
    const body = raw?.data ?? raw
    rows.value = body?.records ?? []
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

function switchRole(role: string) {
  activeRole.value = role
  query.pageNum = 1
  load()
}

function openCreate() {
  Object.assign(createForm, {
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    roleCode: activeRole.value,
    gender: 1,
    campusId: '',
    buildingId: ''
  })
  createVisible.value = true
}

const createLoading = ref(false)
const resetLoading = ref(false)

async function submitCreate() {
  if (!createForm.username || !createForm.password || !createForm.realName) {
    ElMessage.warning('请填写账号、密码和姓名')
    return
  }
  try {
    createLoading.value = true
    await authApi.adminUserCreate(createForm)
    ElMessage.success('新增成功')
    createVisible.value = false
    load()
  } catch {} finally {
    createLoading.value = false
  }
}

async function toggleStatus(row: any) {
  const nextStatus = row.status === 1 ? 0 : 1
  try {
    await authApi.adminUserStatus(row.id, nextStatus)
    ElMessage.success(nextStatus === 1 ? '已启用' : '已禁用')
    load()
  } catch {}
}

function openResetPassword(row: any) {
  currentUser.value = row
  resetPassword.value = ''
  resetVisible.value = true
}

async function submitResetPassword() {
  if (!resetPassword.value) {
    ElMessage.warning('请输入新密码')
    return
  }
  try {
    resetLoading.value = true
    await authApi.adminUserResetPassword(currentUser.value.id, resetPassword.value)
    ElMessage.success('密码已重置')
    resetVisible.value = false
  } catch {} finally {
    resetLoading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">用户管理</div><div class="en">Staff Users</div><div class="hs-waterline"></div></div>

    <div class="hs-panel">
      <el-tabs v-model="activeRole" @tab-change="switchRole">
        <el-tab-pane label="宿舍管理员管理" name="DORM_MANAGER" />
        <el-tab-pane label="维修工管理" name="REPAIRER" />
        <el-tab-pane label="清洁工管理" name="CLEANER" />
      </el-tabs>

      <el-form inline @submit.prevent>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" clearable placeholder="账号/姓名/手机号" style="width:220px" />
        </el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="primary" @click="openCreate"><el-icon aria-hidden="true"><Plus /></el-icon>新增{{ roleMap[activeRole] }}</el-button>
      </el-form>

      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="编号" width="80" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link :type="row.status === 1 ? 'warning' : 'success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="primary" @click="openResetPassword(row)">重置密码</el-button>
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

    <el-dialog v-model="createVisible" :title="`新增${roleMap[activeRole]}`" width="560px" append-to-body destroy-on-close>
      <el-form :model="createForm" label-width="90px">
        <el-form-item label="账号" required><el-input v-model="createForm.username" /></el-form-item>
        <el-form-item label="密码" required><el-input v-model="createForm.password" type="password" show-password /></el-form-item>
        <el-form-item label="姓名" required><el-input v-model="createForm.realName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="createForm.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="createForm.email" /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="createForm.gender">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="校区ID"><el-input v-model="createForm.campusId" /></el-form-item>
        <el-form-item label="楼栋ID"><el-input v-model="createForm.buildingId" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="createLoading" :disabled="createLoading" @click="submitCreate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="resetVisible" title="重置密码" width="420px" append-to-body destroy-on-close>
      <el-form label-width="90px">
        <el-form-item label="新密码"><el-input v-model="resetPassword" type="password" show-password /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetVisible = false">取消</el-button>
        <el-button type="primary" :loading="resetLoading" :disabled="resetLoading" @click="submitResetPassword">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>