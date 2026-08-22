import { createRouter, createWebHistory } from 'vue-router'
import { TOKEN_KEY } from '@/api/http'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: () => import('@/views/Login.vue'), meta: { title: '登录', public: true } },
    {
      path: '/',
      component: () => import('@/layout/AdminLayout.vue'),
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '总览' } },
        { path: 'auth/menus', component: () => import('@/views/auth/MenuManage.vue'), meta: { title: '菜单管理' } },
        { path: 'auth/roles', component: () => import('@/views/auth/RoleManage.vue'), meta: { title: '角色管理' } },
        { path: 'auth/users', component: () => import('@/views/auth/UserManage.vue'), meta: { title: '用户管理' } },
        { path: 'profile', component: () => import('@/views/auth/Profile.vue'), meta: { title: '个人中心' } },
        { path: 'system/dict-type', component: () => import('@/views/system/DictType.vue'), meta: { title: '字典类型' } },
        { path: 'system/dict-data', component: () => import('@/views/system/DictData.vue'), meta: { title: '字典数据' } },
        { path: 'system/config', component: () => import('@/views/system/Config.vue'), meta: { title: '参数配置' } },
        { path: 'system/files', component: () => import('@/views/system/FileCenter.vue'), meta: { title: '文件中心' } },
        { path: 'system/login-logs', component: () => import('@/views/system/LogTable.vue'), meta: { title: '登录日志', logType: 'login' } },
        { path: 'system/operation-logs', component: () => import('@/views/system/LogTable.vue'), meta: { title: '操作日志', logType: 'operation' } },
        { path: 'system/exception-logs', component: () => import('@/views/system/LogTable.vue'), meta: { title: '异常日志', logType: 'exception' } },
        { path: 'dormitory/campus', component: () => import('@/views/dormitory/Campus.vue'), meta: { title: '校区管理' } },
        { path: 'dormitory/building', component: () => import('@/views/dormitory/Building.vue'), meta: { title: '楼栋管理' } },
        { path: 'dormitory/room', component: () => import('@/views/dormitory/Room.vue'), meta: { title: '房间管理' } },
        { path: 'dormitory/record', component: () => import('@/views/dormitory/Record.vue'), meta: { title: '住宿记录' } },
        { path: 'dormitory/my', component: () => import('@/views/dormitory/MyDorm.vue'), meta: { title: '我的宿舍' } },
        { path: 'utility/water', component: () => import('@/views/utility/Water.vue'), meta: { title: '水费余额' } },
        { path: 'utility/electric', component: () => import('@/views/utility/Electric.vue'), meta: { title: '电费余额' } },
        { path: 'utility/payment', component: () => import('@/views/utility/Payment.vue'), meta: { title: '缴费记录' } },
        { path: 'attendance/my', component: () => import('@/views/attendance/MyAttendance.vue'), meta: { title: '我的考勤' } },
        { path: 'attendance/admin', component: () => import('@/views/attendance/AdminAttendance.vue'), meta: { title: '考勤管理' } },
        { path: 'leave', component: () => import('@/views/leave/LeaveList.vue'), meta: { title: '请假管理' } },
        { path: 'repair/my', component: () => import('@/views/repair/MyRepair.vue'), meta: { title: '我的报修' } },
        { path: 'repair/admin', component: () => import('@/views/repair/AdminRepair.vue'), meta: { title: '报修管理' } },
        { path: 'repair/worker', component: () => import('@/views/repair/WorkerRepair.vue'), meta: { title: '我的维修' } },
        { path: 'task/list', component: () => import('@/views/task/CleanTask.vue'), meta: { title: '保洁任务' } },
        { path: 'task/template', component: () => import('@/views/task/TaskTemplate.vue'), meta: { title: '任务模板' } },
        { path: 'evaluation/questionnaire', component: () => import('@/views/evaluation/Questionnaire.vue'), meta: { title: '问卷管理' } },
        { path: 'evaluation/my', component: () => import('@/views/evaluation/MyEvaluation.vue'), meta: { title: '待我评价' } },
        { path: 'evaluation/statistics', component: () => import('@/views/evaluation/Statistics.vue'), meta: { title: '评价统计' } },
        { path: 'notice/admin', component: () => import('@/views/message/NoticeAdmin.vue'), meta: { title: '公告管理' } },
        { path: 'notice/center', component: () => import('@/views/message/NoticeCenter.vue'), meta: { title: '通知中心' } },
        { path: 'message/inbox', component: () => import('@/views/message/Inbox.vue'), meta: { title: '我的消息' } }
      ]
    }
  ]
})

router.beforeEach((to) => {
  if (!to.meta.public && !localStorage.getItem(TOKEN_KEY)) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && localStorage.getItem(TOKEN_KEY)) {
    return '/dashboard'
  }
  return true
})

export default router