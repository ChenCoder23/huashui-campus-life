<script setup lang="ts">
import { onMounted } from 'vue'

const THEME_KEY = 'app-shell-theme'

function resolveInitialTheme() {
  try {
    const saved = localStorage.getItem(THEME_KEY)
    if (saved === 'light' || saved === 'dark') return saved
  } catch {}
  return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
}

onMounted(() => {
  if (!document.documentElement.dataset.theme) {
    document.documentElement.dataset.theme = resolveInitialTheme()
  }
  document.documentElement.classList.toggle('dark', document.documentElement.dataset.theme === 'dark')
})
</script>

<template>
  <router-view />
</template>