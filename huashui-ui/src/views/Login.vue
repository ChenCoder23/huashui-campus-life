<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api'
import { useAuthStore } from '@/store/auth'

import carousel1 from '@/assets/login-carousel/1552382688_2217_thumb.jpg'
import carousel2 from '@/assets/login-carousel/1552382688_5438_thumb.jpg'
import carousel3 from '@/assets/login-carousel/1552382688_7673_thumb.jpg'
import carousel4 from '@/assets/login-carousel/15c1b3164f69432582e1e90ad4333f4e.jpeg'
import carousel5 from '@/assets/login-carousel/3934b471879440a2b71f38bba902d14a.jpeg'
import carousel6 from '@/assets/login-carousel/e439ba2ccce84c9495720471487d96ab.jpeg'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const tab = ref<'account' | 'email'>('account')
const captchaImage = ref('')
const captchaKey = ref('')
const loading = ref(false)
const accountForm = reactive({ account: '', password: '', captchaCode: '' })
const emailForm = reactive({ email: '', code: '' })
const sending = ref(false)

const carouselImages = [carousel1, carousel2, carousel3, carousel4, carousel5, carousel6]

async function loadCaptcha() {
  const res: any = await authApi.captcha()
  captchaImage.value = res.data.captchaImage
  captchaKey.value = res.data.captchaKey
}

async function login() {
  if (!accountForm.account || !accountForm.password || !accountForm.captchaCode) {
    ElMessage.warning('请填写完整登录信息')
    return
  }
  loading.value = true
  try {
    const res: any = await authApi.login({ ...accountForm, captchaKey: captchaKey.value, loginType: 'ACCOUNT' })
    auth.setLogin(res.data)
    ElMessage.success('登录成功')
    router.replace((route.query.redirect as string) || '/dashboard')
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
  <div class="login-page">
    <el-carousel class="login-carousel" height="100%" :interval="3000" arrow="never" indicator-position="none">
      <el-carousel-item v-for="img in carouselImages" :key="img">
        <img :src="img" class="carousel-image" alt="校园风景" />
      </el-carousel-item>
    </el-carousel>


    <div class="login-card hs-panel">
      <div class="brand-line">
        <div>
          <h1>校园生活服务平台</h1>
          <p>HUASHUI CAMPUS LIFE</p>
        </div>
      </div>

      <el-tabs v-model="tab" stretch>
        <el-tab-pane label="账号登录" name="account">
          <el-form @submit.prevent>
            <el-form-item>
              <el-input v-model="accountForm.account" placeholder="学号 / 工号 / 手机号 / 邮箱" size="large" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="accountForm.password" type="password" placeholder="密码" size="large" show-password />
            </el-form-item>
            <el-form-item>
              <div class="captcha-row">
                <el-input v-model="accountForm.captchaCode" placeholder="验证码" size="large" />
                <img v-if="captchaImage" :src="captchaImage" class="captcha" title="点击刷新" @click="loadCaptcha" />
              </div>
            </el-form-item>
            <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="login">登录</el-button>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="邮箱登录" name="email">
          <el-form @submit.prevent>
            <el-form-item>
              <el-input v-model="emailForm.email" placeholder="邮箱" size="large" />
            </el-form-item>
            <el-form-item>
              <div class="captcha-row">
                <el-input v-model="emailForm.code" placeholder="邮箱验证码" size="large" />
                <el-button size="large" :loading="sending" @click="sendCode">发送验证码</el-button>
              </div>
            </el-form-item>
            <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="emailLogin">登录</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="login-foot">统一身份认证 · 华北水利水电大学校园生活服务</div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 24px;
  overflow: hidden;
  background: linear-gradient(135deg, rgba(11, 60, 93, 0.95), rgba(29, 106, 150, 0.75));
}
.login-carousel {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}
.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.login-card {
  position: relative;
  z-index: 2;
  width: min(420px, 100%);
  padding: 34px;
}
.brand-line {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 24px;
}
h1 {
  margin: 0;
  font-size: 22px;
  color: var(--hs-deep);
}
p {
  margin: 3px 0 0;
  font-size: 11px;
  letter-spacing: .18em;
  color: var(--hs-muted);
}
.captcha-row {
  display: flex;
  gap: 10px;
  width: 100%;
}
.captcha {
  height: 40px;
  width: 120px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #dfe7eb;
  cursor: pointer;
}
.login-btn {
  width: 100%;
  margin-top: 6px;
}
.login-foot {
  margin-top: 20px;
  text-align: center;
  color: var(--hs-muted);
  font-size: 12px;
}
</style>