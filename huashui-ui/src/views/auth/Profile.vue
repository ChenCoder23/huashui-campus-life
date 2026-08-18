<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi, storageApi } from '@/api'
import { useAuthStore } from '@/store/auth'

const auth = useAuthStore()
const menus = ref<any[]>([])
const passwordForm = reactive({ account: '', oldPassword: '', newPassword: '', confirmPassword: '', captchaKey: '', captchaCode: '' })
const emailForm = reactive({ email: '', code: '' })
const uploading = ref(false)

const avatarUrl = computed(() => auth.profile?.avatar || '')
const avatarText = computed(() => (auth.profile?.realName || auth.profile?.username || '华').slice(0, 1))

async function load() {
  const res: any = await authApi.profileMenus()
  menus.value = res.data || []
}

async function changePassword() {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  try {
    await authApi.updatePassword(passwordForm)
    ElMessage.success('密码已更新')
  } catch {}
}

async function bindEmail() {
  try {
    await authApi.bindEmail(emailForm)
    ElMessage.success('邮箱已绑定')
  } catch {}
}

async function uploadAvatar(options: any) {
  const formData = new FormData()
  formData.append('file', options.file)
  uploading.value = true
  try {
    const res: any = await storageApi.upload(formData, 'AVATAR')
    const url = res?.data?.url || res?.url
    if (!url) throw new Error('上传失败，未返回头像地址')
    await authApi.updateAvatar(url)
    auth.updateAvatar(url)
    ElMessage.success('头像已更新')
    options.onSuccess(res)
  } catch (error: any) {
    ElMessage.error(error?.message || '头像上传失败')
    options.onError(error)
  } finally {
    uploading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">个人中心</div><div class="en">Profile</div><div class="hs-waterline"></div></div>

    <div class="hs-panel profile-card">
      <el-avatar :size="72" :src="avatarUrl" class="profile-avatar">{{ avatarText }}</el-avatar>
      <div class="profile-meta">
        <div class="profile-name">{{ auth.profile?.realName || auth.profile?.username || '华水用户' }}</div>
        <el-upload
          :show-file-list="false"
          accept="image/jpeg,image/png,image/webp"
          :http-request="uploadAvatar"
          :disabled="uploading"
        >
          <el-button :loading="uploading" type="primary" plain>上传头像</el-button>
        </el-upload>
        <div class="profile-tip">支持 JPG / PNG / WebP，大小不超过 5MB</div>
      </div>
    </div>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <div class="hs-panel">
          <h3>修改密码</h3>
          <el-form :model="passwordForm" label-width="90px">
            <el-form-item label="账号"><el-input v-model="passwordForm.account" /></el-form-item>
            <el-form-item label="原密码"><el-input v-model="passwordForm.oldPassword" type="password" show-password /></el-form-item>
            <el-form-item label="新密码"><el-input v-model="passwordForm.newPassword" type="password" show-password /></el-form-item>
            <el-form-item label="确认密码"><el-input v-model="passwordForm.confirmPassword" type="password" show-password /></el-form-item>
            <el-form-item label="验证码"><el-input v-model="passwordForm.captchaCode" /></el-form-item>
            <el-button type="primary" @click="changePassword">保存</el-button>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="hs-panel">
          <h3>绑定 / 更换邮箱</h3>
          <el-form :model="emailForm" label-width="90px">
            <el-form-item label="邮箱"><el-input v-model="emailForm.email" /></el-form-item>
            <el-form-item label="验证码"><el-input v-model="emailForm.code" /></el-form-item>
            <el-button type="primary" @click="bindEmail">保存</el-button>
          </el-form>
        </div>
        <div class="hs-panel" style="margin-top:16px">
          <h3>我的权限菜单</h3>
          <el-tree :data="menus" node-key="id" :props="{ label: 'menuName', children: 'children' }" />
        </div>
      </el-col>
    </el-row>
  </section>
</template>

<style scoped>
.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
}
.profile-avatar {
  background: linear-gradient(135deg, #e8d5b7, #c85c40);
  color: #0b3c5d;
  font-weight: 800;
  font-size: 26px;
}
.profile-name {
  font-size: 20px;
  font-weight: 800;
  color: var(--hs-deep);
  margin-bottom: 8px;
}
.profile-tip {
  margin-top: 8px;
  color: var(--hs-muted);
  font-size: 12px;
}
h3 { margin: 0 0 16px; color: var(--hs-deep); }
</style>