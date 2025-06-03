<template>
  <div class="common-layout">
    <el-container>
      <div class="login-container">
        <div class="login-header">
          <h1 class="app-title">Catch Cash</h1>
          <p class="app-subtitle">您的財務管理助手</p>
        </div>
        <LoginLogic/>
      </div>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import LoginLogic from "@/views/login/LoginLogic.vue";
</script>

<style scoped>
/* Login Page Styles */
.common-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--background-color);
}

.common-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  background-color: var(--secondary-color);
}

.login-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: var(--background-color);
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.app-title {
  font-size: 2.5rem;
  color: var(--secondary-color);
  margin-bottom: 0.5rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

.app-subtitle {
  font-size: 1.2rem;
  color: var(--info-color);
}
</style>
