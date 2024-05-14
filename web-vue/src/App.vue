<template>
  <div class="container">
    <div class="header">
      <RouterLink to="/">頁面1</RouterLink>
      <RouterLink to="/about">頁面2</RouterLink>
    </div>
    <div class="content">
      <router-view v-slot="{ Component }">
        <component :is="Component"/>
      </router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
</script>
<style scoped>
/* 將外層div設置為Flexbox佈局 */
.container {
  display: flex;
  flex-direction: column; /* 將元素垂直排列 */
  height: 100vh; /* 設置高度以填滿整個視窗 */
}

.header {
  background-color: #f0f0f0;
  padding: 10px;
  display: flex;
  justify-content: center;
}

.header a {
  text-decoration: none;
  color: #333;
  margin: 0 10px;
}

.content {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
