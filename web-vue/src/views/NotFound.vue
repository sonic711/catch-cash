<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">頁面未找到</h2>
      <p class="error-message">很抱歉，您要尋找的頁面不存在或已被移除。</p>
      <el-button type="primary" @click="goToHome" class="back-button">返回首頁</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">

import {useRouter} from 'vue-router';

const router = useRouter();
const goToHome = () => {
  router.push('/dashboard');
};
</script>


<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  min-height: 500px;
  text-align: center;
}

.not-found-content {
  max-width: 500px;
  padding: 2rem;
}

.error-code {
  font-size: 6rem;
  font-weight: bold;
  color: #409EFF;
  margin: 0;
  line-height: 1;
}

.error-title {
  font-size: 2rem;
  margin: 1rem 0;
  color: #303133;
}

.error-message {
  font-size: 1.1rem;
  color: #606266;
  margin-bottom: 2rem;
}

.back-button {
  padding: 12px 24px;
  font-size: 1rem;
}
</style>
