<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { dormitoryApi } from '@/api'

const home = ref<any>(null)
const roommates = ref<any[]>([])

onMounted(async () => {
  const [homeRes, roommateRes] = await Promise.all([dormitoryApi.myDorm(), dormitoryApi.myRoommates()])
  home.value = homeRes.data
  roommates.value = roommateRes.data || []
})
</script>

<template>
  <section class="hs-page">
    <div class="hs-page-title"><div class="cn">我的宿舍</div><div class="en">My Dorm</div><div class="hs-waterline"></div></div>
    <div class="hs-panel">
      <el-descriptions v-if="home" :column="3" border>
        <el-descriptions-item label="校区">{{ home.campusName || '—' }}</el-descriptions-item>
        <el-descriptions-item label="楼栋">{{ home.buildingName || '—' }}</el-descriptions-item>
        <el-descriptions-item label="房间">{{ home.roomNumber || '—' }}</el-descriptions-item>
        <el-descriptions-item label="床位">{{ home.bedNumber || '—' }}</el-descriptions-item>
        <el-descriptions-item label="楼层">{{ home.floorNumber || '—' }}</el-descriptions-item>
        <el-descriptions-item label="房型">{{ home.roomType || '—' }}</el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="暂无宿舍信息" />
    </div>
    <div class="hs-panel" style="margin-top:16px">
      <h3>我的室友</h3>
      <el-table :data="roommates" border>
        <el-table-column prop="bedNumber" label="床位" />
        <el-table-column prop="studentName" label="姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column label="头像">
          <template #default="{ row }">
            <el-avatar :size="32" :src="row.avatar" class="roommate-avatar">{{ (row.studentName || row.studentId || '').slice(0, 1) }}</el-avatar>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<style scoped>
.roommate-avatar {
  background: linear-gradient(135deg, #e8d5b7, #c85c40);
  color: #0b3c5d;
  font-weight: 700;
}
h3 { margin: 0 0 16px; color: var(--hs-deep); }
</style>