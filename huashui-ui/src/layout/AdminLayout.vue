<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { authApi, messageApi } from '@/api'

interface MenuNode {
  index?: string
  label: string
  icon?: string
  roles?: string[]
  children?: MenuNode[]
}

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const unreadCount = ref(0)

const allMenus: MenuNode[] = [
  { index: '/dashboard', label: '总览', icon: 'DataLine' },
  {
    label: '认证权限', icon: 'Lock', children: [
      { index: '/auth/menus', label: '菜单管理', roles: ['SUPER_ADMIN'] },
      { index: '/auth/roles', label: '角色管理', roles: ['SUPER_ADMIN'] },
      { index: '/auth/users', label: '用户管理', roles: ['SUPER_ADMIN'] },
      { index: '/profile', label: '个人中心' }
    ]
  },
  {
    label: '系统管理', icon: 'Setting', roles: ['SUPER_ADMIN'], children: [
      { index: '/system/dict-type', label: '字典类型' },
      { index: '/system/dict-data', label: '字典数据' },
      { index: '/system/config', label: '参数配置' },
      { index: '/system/files', label: '文件中心' },
      { index: '/system/login-logs', label: '登录日志' },
      { index: '/system/operation-logs', label: '操作日志' },
      { index: '/system/exception-logs', label: '异常日志' }
    ]
  },
  {
    label: '宿舍管理', icon: 'OfficeBuilding', children: [
      { index: '/dormitory/campus', label: '校区管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/dormitory/building', label: '楼栋管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/dormitory/room', label: '房间管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/dormitory/record', label: '住宿记录', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/dormitory/my', label: '我的宿舍', roles: ['STUDENT'] }
    ]
  },
  {
    label: '生活服务', icon: 'Coin', roles: ['SUPER_ADMIN', 'DORM_MANAGER', 'STUDENT'], children: [
      { index: '/utility/water', label: '水费余额' },
      { index: '/utility/electric', label: '电费余额' },
      { index: '/utility/payment', label: '缴费记录' }
    ]
  },
  {
    label: '考勤请假', icon: 'Calendar', children: [
      { index: '/attendance/my', label: '我的考勤', roles: ['CLEANER', 'REPAIRER'] },
      { index: '/attendance/admin', label: '考勤管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/leave', label: '请假管理' }
    ]
  },
  {
    label: '维修与保洁', icon: 'Tools', children: [
      { index: '/repair/my', label: '我的报修', roles: ['STUDENT'] },
      { index: '/repair/admin', label: '报修管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/repair/worker', label: '我的维修', roles: ['REPAIRER'] },
      { index: '/task/list', label: '保洁任务', roles: ['SUPER_ADMIN', 'DORM_MANAGER', 'CLEANER'] },
      { index: '/task/template', label: '任务模板', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] }
    ]
  },
  {
    label: '评价问卷', icon: 'EditPen', children: [
      { index: '/evaluation/questionnaire', label: '问卷管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/evaluation/my', label: '待我评价', roles: ['STUDENT'] },
      { index: '/evaluation/statistics', label: '评价统计', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] }
    ]
  },
  {
    label: '消息公告', icon: 'Bell', children: [
      { index: '/notice/admin', label: '公告管理', roles: ['SUPER_ADMIN', 'DORM_MANAGER'] },
      { index: '/notice/center', label: '通知中心' },
      { index: '/message/inbox', label: '我的消息' }
    ]
  }
]

const currentRole = computed(() => auth.profile?.userType || 'STUDENT')

function filterMenus(nodes: MenuNode[]): MenuNode[] {
  const result: MenuNode[] = []
  for (const node of nodes) {
    if (node.roles && !node.roles.includes(currentRole.value)) continue
    const children = node.children ? filterMenus(node.children) : []
    if (node.children && children.length === 0) continue
    result.push({ ...node, children: node.children ? children : undefined })
  }
  return result
}

const menus = computed(() => filterMenus(allMenus))
const activeIndex = computed(() => route.path)
const displayName = computed(() => auth.profile?.realName || auth.profile?.username || '华水用户')
const avatarUrl = computed(() => auth.profile?.avatar || '')
const avatarText = computed(() => (displayName.value || '华').slice(0, 1))

async function loadUnread() {
  try {
    const res: any = await messageApi.unreadCount()
    unreadCount.value = Number(res?.data ?? 0)
  } catch {
    unreadCount.value = 0
  }
}

function goInbox() {
  router.push('/message/inbox')
}

async function logout() {
  await ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
  try { await authApi.logout() } catch {}
  auth.logout()
  router.replace('/login')
}

onMounted(loadUnread)
</script>

<template>
  <el-container class="admin">
    <el-aside width="248px" class="sidebar">
      <div class="brand">
        <img src="@/assets/logo.svg" class="brand-logo" alt="华水校园生活服务平台" />
        <div>
          <div class="brand-title">校园生活服务平台</div>
          <div class="brand-sub">HUASHUI CAMPUS LIFE</div>
        </div>
      </div>
      <el-scrollbar class="menu-scroll">
        <el-menu :default-active="activeIndex" router background-color="transparent" text-color="#d7e9f3" active-text-color="#ffffff">
          <template v-for="item in menus" :key="item.index || item.label">
            <el-sub-menu v-if="item.children" :index="item.label">
              <template #title>
                <el-icon><component :is="item.icon" /></el-icon>
                <span>{{ item.label }}</span>
              </template>
              <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">{{ child.label }}</el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="item.index">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.label }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-title">{{ route.meta.title || '华水校园生活' }}</div>
        <div class="header-right">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="inbox-badge">
            <el-button circle :icon="'Bell'" title="收件箱" @click="goInbox" />
          </el-badge>
          <el-avatar :size="32" :src="avatarUrl" class="user-avatar">{{ avatarText }}</el-avatar>
          <span class="user-name">{{ displayName }}</span>
          <el-button link type="danger" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.admin {
  height: 100vh;
}
.sidebar {
  background: linear-gradient(180deg, #0b3c5d 0%, #123f66 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
}
.brand {
  height: 76px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 18px;
  border-bottom: 1px solid rgba(255,255,255,.12);
}
.brand-logo {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0,0,0,.18);
}
.brand-mark {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #e8d5b7, #c85c40);
  color: #0b3c5d;
  font-weight: 900;
  display: grid;
  place-items: center;
  letter-spacing: .04em;
}
.brand-title {
  font-weight: 800;
  letter-spacing: .04em;
}
.brand-sub {
  font-size: 10px;
  letter-spacing: .18em;
  color: rgba(255,255,255,.55);
}
.menu-scroll {
  flex: 1;
}
.sidebar :deep(.el-menu) {
  border-right: none;
}
.sidebar :deep(.el-menu-item.is-active) {
  background: rgba(255,255,255,.12);
  border-left: 3px solid #e8d5b7;
}
.header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255,255,255,.78);
  border-bottom: 1px solid rgba(11,60,93,.08);
  padding: 0 24px;
}
.header-title {
  font-weight: 800;
  color: var(--hs-deep);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.inbox-badge {
  margin-right: 4px;
}
.user-avatar {
  background: linear-gradient(135deg, #e8d5b7, #c85c40);
  color: #0b3c5d;
  font-weight: 700;
  flex-shrink: 0;
}
.user-name {
  color: var(--hs-water);
  font-weight: 600;
}
.main {
  overflow: auto;
  padding: 22px;
}
</style>