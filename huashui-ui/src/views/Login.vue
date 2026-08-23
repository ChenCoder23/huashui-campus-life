<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'
import { useAuthStore } from '@/store/auth'
import { useTheme } from '@/composables/useTheme'
import campusImage from '@/assets/login-carousel/1552382688_5438_thumb.jpg'
import './login.css'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const { isDark, themeLabel, toggleTheme } = useTheme()
const tab = ref<'account' | 'email'>('account')
const captchaImage = ref('')
const captchaKey = ref('')
const loading = ref(false)
const sending = ref(false)
const accountForm = reactive({ account: '', password: '', captchaCode: '' })
const emailForm = reactive({ email: '', code: '' })

async function loadCaptcha() {
  try {
    const res: any = await authApi.captcha()
    captchaImage.value = res.data.captchaImage
    captchaKey.value = res.data.captchaKey
    accountForm.captchaCode = ''
  } catch {
    ElMessage.error('验证码加载失败，请稍后重试')
  }
}

async function login() {
  if (!accountForm.account || !accountForm.password || !accountForm.captchaCode) {
    ElMessage.warning('请填写账号、密码和验证码')
    return
  }
  loading.value = true
  try {
    const res: any = await authApi.login({ ...accountForm, captchaKey: captchaKey.value, loginType: 'ACCOUNT' })
    auth.setLogin(res.data)
    ElMessage.success('登录成功')
    router.replace((route.query.redirect as string) || '/dashboard')
  } catch {
    try { await loadCaptcha() } catch {}
  } finally {
    loading.value = false
  }
}

async function sendCode() {
  if (!emailForm.email) {
    ElMessage.warning('请输入邮箱')
    return
  }
  sending.value = true
  try {
    await authApi.sendEmailCode(emailForm.email)
    ElMessage.success('验证码已发送')
  } finally {
    sending.value = false
  }
}

async function emailLogin() {
  if (!emailForm.email || !emailForm.code) {
    ElMessage.warning('请输入邮箱和验证码')
    return
  }
  loading.value = true
  try {
    const res: any = await authApi.emailLogin(emailForm)
    auth.setLogin(res.data)
    ElMessage.success('登录成功')
    router.replace((route.query.redirect as string) || '/dashboard')
  } finally {
    loading.value = false
  }
}

onMounted(loadCaptcha)
</script>

<template>
  <main class="login-page">
    <section class="login-visual" aria-label="校园风景">
      <img :src="campusImage" alt="华北水利水电大学校园湖畔与教学楼" width="800" height="500" />
      <div class="visual-copy">
        <div class="visual-eyebrow">华北水利水电大学</div>
        <h2>校园生活服务统一入口</h2>
        <p>宿舍、报修、考勤、缴费与通知服务。</p>
      </div>
    </section>

    <section class="login-workspace" aria-labelledby="login-title">
      <el-button class="login-theme-button" circle :aria-label="themeLabel" :title="themeLabel" @click="toggleTheme">
        <el-icon><Sunny v-if="isDark" /><Moon v-else /></el-icon>
      </el-button>

      <div class="login-card">
        <div class="brand-line">
          <img src="@/assets/logo.svg" class="brand-logo" alt="" aria-hidden="true" />
          <div><h1>校园生活服务平台</h1><p>HUASHUI CAMPUS LIFE</p></div>
        </div>
        <div class="login-heading">
          <h2 id="login-title">欢迎回来</h2>
          <p>使用校园统一身份信息登录工作台</p>
        </div>

        <el-tabs v-model="tab" class="login-tabs" stretch>
          <el-tab-pane label="账号登录" name="account">
            <el-form class="login-form" @submit.prevent="login">
              <el-form-item>
                <div class="field-stack">
                  <label class="field-label" for="login-account">账号</label>
                  <el-input id="login-account" v-model="accountForm.account" autocomplete="username" placeholder="学号、工号、手机号或邮箱" size="large" />
                </div>
              </el-form-item>
              <el-form-item>
                <div class="field-stack">
                  <label class="field-label" for="login-password">密码</label>
                  <el-input id="login-password" v-model="accountForm.password" autocomplete="current-password" type="password" placeholder="请输入密码" size="large" show-password />
                </div>
              </el-form-item>
              <el-form-item>
                <div class="field-stack">
                  <label class="field-label" for="login-captcha">图形验证码</label>
                  <div class="captcha-row">
                    <el-input id="login-captcha" v-model="accountForm.captchaCode" autocomplete="off" placeholder="请输入验证码" size="large" />
                    <button v-if="captchaImage" type="button" class="captcha-button" aria-label="刷新图形验证码" @click="loadCaptcha">
                      <img :src="captchaImage" class="captcha" alt="图形验证码，点击刷新" width="120" height="44" />
                    </button>
                  </div>
                </div>
              </el-form-item>
              <el-button native-type="submit" type="primary" size="large" class="login-btn" :loading="loading" :disabled="loading">登录</el-button>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="邮箱登录" name="email">
            <el-form class="login-form" @submit.prevent="emailLogin">
              <el-form-item>
                <div class="field-stack">
                  <label class="field-label" for="login-email">邮箱</label>
                  <el-input id="login-email" v-model="emailForm.email" autocomplete="email" type="email" placeholder="请输入已绑定邮箱" size="large" />
                </div>
              </el-form-item>
              <el-form-item>
                <div class="field-stack">
                  <label class="field-label" for="login-email-code">邮箱验证码</label>
                  <div class="captcha-row">
                    <el-input id="login-email-code" v-model="emailForm.code" autocomplete="one-time-code" placeholder="请输入验证码" size="large" />
                    <el-button size="large" :loading="sending" :disabled="sending" @click="sendCode">发送验证码</el-button>
                  </div>
                </div>
              </el-form-item>
              <el-button native-type="submit" type="primary" size="large" class="login-btn" :loading="loading" :disabled="loading">登录</el-button>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <div class="login-foot">统一身份认证 · 华北水利水电大学校园生活服务</div>
      </div>
    </section>
  </main>
</template>
