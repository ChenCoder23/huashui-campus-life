import { computed, ref } from 'vue'

export type AppTheme = 'light' | 'dark'

const THEME_KEY = 'app-shell-theme'
const currentTheme = ref<AppTheme>('light')
let initialized = false

function getInitialTheme(): AppTheme {
  try {
    const saved = localStorage.getItem(THEME_KEY)
    if (saved === 'light' || saved === 'dark') return saved
  } catch {}
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

function applyTheme(theme: AppTheme) {
  currentTheme.value = theme
  document.documentElement.dataset.theme = theme
  document.documentElement.classList.toggle('dark', theme === 'dark')
  try { localStorage.setItem(THEME_KEY, theme) } catch {}
}

export function useTheme() {
  if (!initialized) {
    currentTheme.value = (document.documentElement.dataset.theme as AppTheme) || getInitialTheme()
    document.documentElement.dataset.theme = currentTheme.value
    document.documentElement.classList.toggle('dark', currentTheme.value === 'dark')
    initialized = true
  }

  const isDark = computed(() => currentTheme.value === 'dark')
  const themeLabel = computed(() => isDark.value ? '切换到浅色模式' : '切换到深色模式')

  function toggleTheme() {
    applyTheme(isDark.value ? 'light' : 'dark')
  }

  return {
    currentTheme,
    isDark,
    themeLabel,
    toggleTheme
  }
}
