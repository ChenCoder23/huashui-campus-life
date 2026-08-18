import axios from 'axios'
import { ElMessage } from 'element-plus'

export const TOKEN_KEY = 'huashui_satoken'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.set('satoken', token)
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload && typeof payload === 'object' && 'code' in payload && payload.code !== 200) {
      ElMessage.error(payload.message || payload.msg || '请求失败')
      return Promise.reject(new Error(payload.message || payload.msg || '请求失败'))
    }
    return payload
  },
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.response?.data?.msg || error.message || '网络异常'
    if (status === 401) {
      localStorage.removeItem(TOKEN_KEY)
      if (location.pathname !== '/login') {
        location.href = '/login'
      }
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default http