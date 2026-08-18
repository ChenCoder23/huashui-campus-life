import { defineStore } from 'pinia'
import { TOKEN_KEY } from '@/api/http'

interface LoginVO {
  token: string
  userId: number
  username: string
  realName: string
  userType: string
  avatar?: string
}

const PROFILE_KEY = 'huashui_profile'

function loadProfile(): LoginVO | null {
  try {
    const raw = localStorage.getItem(PROFILE_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

function saveProfile(profile: LoginVO | null) {
  if (profile) {
    localStorage.setItem(PROFILE_KEY, JSON.stringify(profile))
  } else {
    localStorage.removeItem(PROFILE_KEY)
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || '',
    profile: loadProfile()
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token)
  },
  actions: {
    setLogin(profile: LoginVO) {
      this.token = profile.token
      this.profile = profile
      localStorage.setItem(TOKEN_KEY, profile.token)
      saveProfile(profile)
    },
    updateAvatar(avatar: string) {
      if (this.profile) {
        this.profile.avatar = avatar
        saveProfile(this.profile)
      }
    },
    logout() {
      this.token = ''
      this.profile = null
      localStorage.removeItem(TOKEN_KEY)
      saveProfile(null)
    }
  }
})