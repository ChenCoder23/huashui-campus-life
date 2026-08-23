<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import { authApi, messageApi } from '@/api'
import { useTheme } from '@/composables/useTheme'
import './app-shell.css'

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
const mobileNavOpen = ref(false)
const mobileNavSize = ref(304)
const { isDark, themeLabel, toggleTheme } = useTheme()

const roleLabels: Record<string, string> = {
  SUPER_ADMIN: '超级管理员',
  DORM_MANAGER: '宿舍管理员',
  STUDENT: '学生',
  CLEANER: '保洁人员',
  REPAIRER: '维修人员'
}

const routeDescriptions: Record<string, string> = {
  '/dashboard': '校园服务运行概览与个人事务入口',
  '/profile': '管理账户安全、头像与联系方式',
  '/auth/menus': '配置平台导航与功能权限',
  '/auth/roles': '维护角色及其菜单授权范围',
  '/auth/users': '管理平台用户、状态与角色',
  '/system/dict-type': '维护平台通用字典分类',
  '/system/dict-data': '维护字典条目与业务枚举',
  '/system/config': '维护平台运行参数',
  '/system/files': '查看与管理平台文件资源',
  '/system/login-logs': '查看用户登录记录',
  '/system/operation-logs': '查看关键业务操作记录',
  '/system/exception-logs': '排查平台异常与处理状态',
  '/dormitory/campus': '维护校区基础信息',
  '/dormitory/building': '维护楼栋与配套配置',
  '/dormitory/room': '维护宿舍房间与床位',
  '/dormitory/record': '管理学生住宿分配记录',
  '/dormitory/my': '查看当前住宿信息与室友',
  '/utility/water': '查看宿舍水费余额与用量',
  '/utility/electric': '查看宿舍电费余额与用量',
  '/utility/payment': '查看校园生活缴费记录',
  '/attendance/my': '完成签到并查看个人考勤',
  '/attendance/admin': '汇总并管理人员考勤',
  '/leave': '提交、审核与跟踪请假申请',
  '/repair/my': '提交并跟踪个人报修',
  '/repair/admin': '分派与管理报修工单',
  '/repair/worker': '处理分配给我的维修任务',
  '/task/list': '安排并跟踪保洁任务',
  '/task/template': '维护常用保洁任务模板',
  '/evaluation/questionnaire': '创建并管理评价问卷',
  '/evaluation/my': '完成待办评价问卷',
  '/evaluation/statistics': '查看问卷回收与评分结果',
  '/notice/admin': '起草、发布与管理公告',
  '/notice/center': '查看校园最新通知公告',
  '/message/inbox': '查看个人消息与待办提醒'
}

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
const roleLabel = computed(() => roleLabels[currentRole.value] || currentRole.value)
const pageTitle = computed(() => String(route.meta.title || '华水校园生活'))
const pageDescription = computed(() => routeDescriptions[route.path] || '集中处理校园生活服务事项')

async function loadUnread() {
  try {
    const res: any = await messageApi.unreadCount()
    unreadCount.value = Number(res?.data ?? 0)
  } catch {
    unreadCount.value = 0
  }
}

function updateViewport() {
  mobileNavSize.value = Math.min(304, Math.max(260, window.innerWidth - 40))
  if (window.innerWidth >= 960) mobileNavOpen.value = false
}

function goInbox() {
  router.push('/message/inbox')
}

async function logout() {
  try {
    await ElMessageBox.confirm('确定退出登录吗？', '退出登录', { type: 'warning' })
  } catch {
    return
  }
  try { await authApi.logout() } catch {}
  auth.logout()
  router.replace('/login')
}

function handleUserCommand(command: string) {
  if (command === 'profile') router.push('/profile')
  if (command === 'logout') logout()
}

watch(() => route.fullPath, async () => {
  mobileNavOpen.value = false
  await nextTick()
  document.getElementById('main-content')?.focus({ preventScroll: true })
})

onMounted(() => {
  loadUnread()
  updateViewport()
  window.addEventListener('resize', updateViewport)
})

onBeforeUnmount(() => window.removeEventListener('resize', updateViewport))
</script>

<template>
  <el-container class="app-shell">
    <el-aside class="app-sidebar app-sidebar--desktop">
      <div class="app-brand">
        <img src="@/assets/logo.svg" class="app-brand__logo" alt="" aria-hidden="true" />
        <div class="app-brand__copy">
          <div class="app-brand__title">校园生活服务平台</div>
          <div class="app-brand__sub">HUASHUI CAMPUS LIFE</div>
        </div>
      </div>

      <el-scrollbar class="app-menu-scroll">
        <el-menu :default-active="activeIndex" router>
          <template v-for="item in menus" :key="item.index || item.label">
            <el-sub-menu v-if="item.children" :index="item.label">
              <template #title>
                <el-icon aria-hidden="true"><component :is="item.icon" /></el-icon>
                <span>{{ item.label }}</span>
              </template>
              <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">{{ child.label }}</el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="item.index">
              <el-icon aria-hidden="true"><component :is="item.icon" /></el-icon>
              <span>{{ item.label }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>

      <div class="app-sidebar__footer">
        <div class="app-sidebar-user">
          <el-avatar :size="32" :src="avatarUrl" class="app-user-avatar">{{ avatarText }}</el-avatar>
          <div class="app-sidebar-user__copy">
            <div class="app-sidebar-user__name">{{ displayName }}</div>
            <div class="app-sidebar-user__role">{{ roleLabel }}</div>
          </div>
        </div>
      </div>
    </el-aside>

    <el-container class="app-workspace">
      <el-header class="app-header">
        <div class="app-header__context">
          <el-button class="app-mobile-menu app-icon-button" circle aria-label="打开导航菜单" @click="mobileNavOpen = true">
            <el-icon><Menu /></el-icon>
          </el-button>
          <div class="app-header__copy">
            <div class="app-header__title">{{ pageTitle }}</div>
            <div class="app-header__subtitle">{{ pageDescription }}</div>
          </div>
        </div>

        <div class="app-header__actions">
          <el-button class="app-icon-button" circle :aria-label="themeLabel" :title="themeLabel" @click="toggleTheme">
            <el-icon><Sunny v-if="isDark" /><Moon v-else /></el-icon>
          </el-button>
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="app-inbox-badge">
            <el-button class="app-icon-button" circle aria-label="打开我的消息" title="我的消息" @click="goInbox">
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>
          <el-dropdown trigger="click" @command="handleUserCommand">
            <button type="button" class="app-user-trigger" aria-label="打开账户菜单">
              <el-avatar :size="30" :src="avatarUrl" class="app-user-avatar">{{ avatarText }}</el-avatar>
              <span class="app-user-trigger__name">{{ displayName }}</span>
              <el-icon aria-hidden="true"><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main id="main-content" class="app-main" tabindex="-1">
        <router-view />
      </el-main>
    </el-container>

    <el-drawer v-model="mobileNavOpen" class="app-mobile-nav" direction="ltr" :size="mobileNavSize" :with-header="false">
      <aside class="app-sidebar app-sidebar--mobile">
        <div class="app-brand">
          <img src="@/assets/logo.svg" class="app-brand__logo" alt="" aria-hidden="true" />
          <div class="app-brand__copy">
            <div class="app-brand__title">校园生活服务平台</div>
            <div class="app-brand__sub">HUASHUI CAMPUS LIFE</div>
          </div>
        </div>
        <el-scrollbar class="app-menu-scroll">
          <el-menu :default-active="activeIndex" router>
            <template v-for="item in menus" :key="item.index || item.label">
              <el-sub-menu v-if="item.children" :index="`mobile-${item.label}`">
                <template #title>
                  <el-icon aria-hidden="true"><component :is="item.icon" /></el-icon>
                  <span>{{ item.label }}</span>
                </template>
                <el-menu-item v-for="child in item.children" :key="child.index" :index="child.index">{{ child.label }}</el-menu-item>
              </el-sub-menu>
              <el-menu-item v-else :index="item.index">
                <el-icon aria-hidden="true"><component :is="item.icon" /></el-icon>
                <span>{{ item.label }}</span>
              </el-menu-item>
            </template>
          </el-menu>
        </el-scrollbar>
        <div class="app-sidebar__footer">
          <div class="app-sidebar-user">
            <el-avatar :size="32" :src="avatarUrl" class="app-user-avatar">{{ avatarText }}</el-avatar>
            <div class="app-sidebar-user__copy">
              <div class="app-sidebar-user__name">{{ displayName }}</div>
              <div class="app-sidebar-user__role">{{ roleLabel }}</div>
            </div>
          </div>
        </div>
      </aside>
    </el-drawer>
  </el-container>
</template>
