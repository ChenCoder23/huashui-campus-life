<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { attendanceApi } from '@/api'

const today = ref<any>(null)
const rows = ref<any[]>([])
const loading = ref(false)
const checkForm = reactive({ checkInType: 'GPS', location: '', photoUrl: '' })

async function load() {
  loading.value = true
  try {
    const [todayRes, myRes] = await Promise.all([attendanceApi.today(), attendanceApi.my()])
    today.value = todayRes.data
    rows.value = myRes.data || []
  } finally { loading.value = false }
}

async function checkIn() {
  if (!checkForm.location) { ElMessage.warning('请输入位置'); return }
  try { await attendanceApi.checkIn(checkForm); ElMessage.success('打卡成功'); load() } catch {}
}

onMounted(load)
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">我的考勤</div><div class="en">My Attendance</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-form inline @submit.prevent>
        <el-form-item label="打卡方式"><el-select v-model="checkForm.checkInType"><el-option label="GPS" value="GPS" /><el-option label="拍照" value="PHOTO" /></el-select></el-form-item>
        <el-form-item label="位置"><el-input v-model="checkForm.location" placeholder="如：龙子湖校区 1 号教学楼" style="width:260px" /></el-form-item>
        <el-form-item label="照片URL"><el-input v-model="checkForm.photoUrl" style="width:220px" /></el-form-item>
        <el-button type="primary" @click="checkIn">立即打卡</el-button>
      </el-form>
      <el-alert v-if="today" :title="`今日状态：${today.checkInStatus || '未打卡'}，打卡时间：${today.checkInTime || '—'}`" type="success" show-icon />
    </div>
    <div class="hs-panel" style="margin-top:16px">
      <el-table :data="rows" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="attendanceDate" label="日期" />
        <el-table-column prop="checkInTime" label="打卡时间" />
        <el-table-column prop="checkInType" label="方式" />
        <el-table-column prop="checkInLocation" label="位置" />
        <el-table-column prop="checkInStatus" label="状态" />
      </el-table>
    </div>
  </section>
</template>