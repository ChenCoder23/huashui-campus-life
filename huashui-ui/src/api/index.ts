import http from './http'

// 认证
export const authApi = {
  captcha: () => http.get('/auth/captcha'),
  login: (data: any) => http.post('/auth/login', data),
  emailLogin: (params: any) => http.post('/auth/email/login', null, { params }),
  sendEmailCode: (email: string) => http.post('/auth/bind/email/send-code', null, { params: { email } }),
  logout: () => http.post('/auth/logout'),
  profileMenus: () => http.get('/auth/profile'),
  updatePassword: (data: any) => http.put('/auth/password', data),
  updateAvatar: (avatarUrl: string) => http.put('/auth/profile/avatar', null, { params: { avatarUrl } }),
  bindEmail: (data: any) => http.post('/auth/email', data),
  updateEmail: (data: any) => http.put('/auth/email', data),
  menus: () => http.get('/auth/menus'),
  createMenu: (data: any) => http.post('/auth/menus', data),
  updateMenu: (data: any) => http.put('/auth/menus', data),
  deleteMenu: (id: number) => http.delete(`/auth/menus/${id}`),
  roleMenus: (id: number) => http.get(`/auth/roles/${id}/menus`),
  setRoleMenus: (id: number, menuIds: number[]) => http.put(`/auth/roles/${id}/menus`, menuIds),
  roles: () => http.get('/auth/roles'),
  updateRole: (id: number, data: any) => http.put(`/auth/roles/${id}`, data),
  userRoles: (id: number) => http.get(`/auth/users/${id}/roles`),
  setUserRoles: (id: number, roleId: number) => http.put(`/auth/users/${id}/roles`, roleId),
  adminUserPage: (params: any) => http.get('/auth/admin/users/page', { params }),
  adminUserCreate: (data: any) => http.post('/auth/admin/users', data),
  adminUserStatus: (id: number, status: number) => http.put(`/auth/admin/users/${id}/status`, null, { params: { status } }),
  adminUserResetPassword: (id: number, password: string) => http.put(`/auth/admin/users/${id}/password`, { password })
}

// 系统
export const systemApi = {
  dictDataPage: (params: any) => http.get('/system/dict-data', { params }),
  createDictData: (data: any) => http.post('/system/dict-data', data),
  updateDictData: (id: number, data: any) => http.put(`/system/dict-data/${id}`, data),
  deleteDictData: (id: number) => http.delete(`/system/dict-data/${id}`),
  dictDataByType: (dictType: string) => http.get(`/dict/data/${dictType}`),
  dictTypePage: (params: any) => http.get('/system/dict-type', { params }),
  createDictType: (data: any) => http.post('/system/dict-type', data),
  updateDictType: (id: number, data: any) => http.put(`/system/dict-type/${id}`, data),
  deleteDictType: (id: number) => http.delete(`/system/dict-type/${id}`),
  configPage: (params: any) => http.get('/system/config', { params }),
  createConfig: (data: any) => http.post('/system/config', data),
  updateConfig: (id: number, data: any) => http.put(`/system/config/${id}`, data),
  deleteConfig: (id: number) => http.delete(`/system/config/${id}`),
  loginLogs: (params: any) => http.get('/system/log/login', { params }),
  operationLogs: (params: any) => http.get('/system/log/operation', { params }),
  exceptionLogs: (params: any) => http.get('/system/log/exception', { params }),
  exceptionDetail: (id: number) => http.get(`/system/log/exception/${id}`)
}

// 存储
export const storageApi = {
  upload: (formData: FormData, type: string) => http.post(`/storage/upload`, formData, { params: { type }, headers: { 'Content-Type': 'multipart/form-data' } }),
  files: (ids: string) => http.get('/storage/files', { params: { ids } }),
  remove: (id: number) => http.delete(`/storage/${id}`)
}

// 宿舍
export const dormitoryApi = {
  campusPage: (params: any) => http.get('/dormitory/campus', { params }),
  createCampus: (data: any) => http.post('/dormitory/campus', data),
  updateCampus: (id: number, data: any) => http.put(`/dormitory/campus/${id}`, data),
  deleteCampus: (id: number) => http.delete(`/dormitory/campus/${id}`),
  campusOptions: () => http.get('/dormitory/campus/options'),
  buildingPage: (params: any) => http.get('/dormitory/building', { params }),
  createBuilding: (data: any) => http.post('/dormitory/building', data),
  updateBuilding: (id: number, data: any) => http.put(`/dormitory/building/${id}`, data),
  deleteBuilding: (id: number) => http.delete(`/dormitory/building/${id}`),
  buildingDetail: (id: number) => http.get(`/dormitory/building/${id}`),
  updateBuildingConfig: (id: number, data: any) => http.put(`/dormitory/building/${id}/config`, data),
  buildingOptions: (campusId?: number) => http.get('/dormitory/building/options', { params: { campusId } }),
  roomPage: (params: any) => http.get('/dormitory/room', { params }),
  createRoom: (data: any) => http.post('/dormitory/room', data),
  updateRoom: (id: number, data: any) => http.put(`/dormitory/room/${id}`, data),
  deleteRoom: (id: number) => http.delete(`/dormitory/room/${id}`),
  roomDetail: (id: number) => http.get(`/dormitory/room/${id}`),
  batchCreateRoom: (data: any) => http.post('/dormitory/room/batch', data),
  recordPage: (params: any) => http.get('/dormitory/record', { params }),
  assignRecord: (data: any) => http.post('/dormitory/record/assign', data),
  adjustRecord: (data: any) => http.post('/dormitory/record/adjust', data),
  checkoutRecord: (studentId: number) => http.post('/dormitory/record/checkout', null, { params: { studentId } }),
  studentRecord: (studentId: number) => http.get(`/dormitory/record/student/${studentId}`),
  importRecord: (fileUrl: string) => http.post('/dormitory/record/import', null, { params: { fileUrl } }),
  exportRecord: (buildingId?: number) => http.get('/dormitory/record/export', { params: { buildingId } }),
  myDorm: () => http.get('/dormitory/home'),
  myRoommates: () => http.get('/dormitory/home/roommates')
}

// 水电缴费
export const utilityApi = {
  waterPage: (params: any) => http.get('/utility/water', { params }),
  electricPage: (params: any) => http.get('/utility/electric', { params }),
  paymentPage: (params: any) => http.get('/utility/payment', { params })
}

// 考勤
export const attendanceApi = {
  today: () => http.get('/attendance/today'),
  my: () => http.get('/attendance/my'),
  checkIn: (data: any) => http.post('/attendance/check-in', data),
  adminPage: (params: any) => http.get('/admin/attendance/page', { params }),
  workerPage: (workerId: number, params: any) => http.get(`/admin/attendance/worker/${workerId}`, { params }),
  adminUpdate: (data: any) => http.put('/admin/attendance/update', data),
  statistics: (params: any) => http.get('/admin/attendance/statistics', { params })
}

// 请假
export const leaveApi = {
  page: (params: any) => http.get('/leave', { params }),
  detail: (id: number) => http.get(`/leave/${id}`),
  submit: (data: any) => http.post('/leave', data),
  approve: (id: number, opinion?: string) => http.put(`/leave/${id}/approve`, null, { params: { opinion } }),
  reject: (id: number, reason?: string) => http.put(`/leave/${id}/reject`, null, { params: { reason } }),
  cancel: (id: number) => http.put(`/leave/${id}/cancel`)
}

// 报修
export const repairApi = {
  adminPage: (params: any) => http.get('/repair/admin/page', { params }),
  adminDetail: (id: number) => http.get(`/repair/admin/${id}`),
  assign: (data: any) => http.put('/repair/admin/assign', data),
  studentCreate: (data: any) => http.post('/repair/student', data),
  studentPage: (params: any) => http.get('/repair/student/page', { params }),
  studentDetail: (id: number) => http.get(`/repair/student/${id}`),
  studentCancel: (id: number) => http.put(`/repair/student/cancel/${id}`),
  workerPage: (params: any) => http.get('/repair/worker/page', { params }),
  workerDetail: (id: number) => http.get(`/repair/worker/${id}`),
  workerStart: (id: number) => http.put(`/repair/worker/start/${id}`),
  workerComplete: (data: any) => http.put('/repair/worker/complete', data)
}

// 保洁任务
export const taskApi = {
  generate: (data: any) => http.post('/task/generate', data),
  list: (params: any) => http.get('/task/list', { params }),
  my: () => http.get('/task/my'),
  detail: (id: number) => http.get(`/task/${id}`),
  updateStatus: (data: any) => http.put('/task/status', data),
  templatePage: (params: any) => http.get('/task/template/page', { params }),
  createTemplate: (data: any) => http.post('/task/template', data),
  updateTemplate: (data: any) => http.put('/task/template', data),
  deleteTemplate: (id: number) => http.delete(`/task/template/${id}`),
  templateDetail: (id: number) => http.get(`/task/template/${id}`)
}

// 评价
export const evaluationApi = {
  questionnairePage: (params: any) => http.get('/evaluation/questionnaire/page', { params }),
  createQuestionnaire: (data: any) => http.post('/evaluation/questionnaire', data),
  questionnaireDetail: (id: number) => http.get(`/evaluation/questionnaire/${id}`),
  updateQuestionnaire: (id: number, data: any) => http.put(`/evaluation/questionnaire/${id}`, data),
  deleteQuestionnaire: (id: number) => http.delete(`/evaluation/questionnaire/${id}`),
  finishQuestionnaire: (id: number) => http.put(`/evaluation/questionnaire/${id}/finish`),
  myResponses: () => http.get('/evaluation/response/list'),
  responseDetail: (questionnaireId: number) => http.get(`/evaluation/response/${questionnaireId}`),
  submitResponse: (questionnaireId: number, data: any) => http.post(`/evaluation/response/${questionnaireId}/submit`, data),
  statistics: (questionnaireId: number) => http.get(`/evaluation/statistics/${questionnaireId}`),
  questionStats: (questionnaireId: number) => http.get(`/evaluation/statistics/${questionnaireId}/questions`)
}

// 消息公告
export const messageApi = {
  adminDraftPage: (params: any) => http.get('/notice/admin/draft', { params }),
  noticeTypes: () => http.get('/notice/admin/types'),
  createNotice: (data: any) => http.post('/notice/admin', data),
  updateNotice: (id: number, data: any) => http.put(`/notice/admin/${id}`, data),
  revokeNotice: (id: number) => http.put(`/notice/admin/${id}/revoke`),
  deleteNotice: (id: number) => http.delete(`/notice/admin/${id}`),
  noticeScroll: (params: any) => http.get('/notice/scroll', { params }),
  noticeLatest: () => http.get('/notice/latest'),
  noticeDetail: (id: number) => http.get(`/notice/${id}`),
  messageScroll: (params: any) => http.get('/message/scroll', { params }),
  unreadCount: () => http.get('/message/unread/count'),
  messageDetail: (id: number) => http.get(`/message/${id}`),
  readAll: () => http.put('/message/read-all')
}