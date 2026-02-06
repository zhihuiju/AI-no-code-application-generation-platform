<template>
  <div class="basic-layout">
    <GlobalHeader />
    <div class="content">
      <router-view />
    </div>
    <GlobalFooter />
  </div>
</template>

<script setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style>
.basic-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding: 24px;
  margin: 0 auto;
  max-width: 1200px;
  width: 100%;
  box-sizing: border-box;
}

@media (max-width: 768px) {
  .content {
    padding: 16px;
  }
}
</style>
