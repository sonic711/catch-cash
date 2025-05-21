<template>
  <div class="common-layout" v-if="!isLoggedIn">
    <el-container>
      <HomePage v-if="!isLoggedIn" @login-success="isLoggedIn = true"/>
    </el-container>
  </div>
  <div v-else>
    <el-container>
      <el-aside width="200px" class="aside-nav">
        <RouterLink to="/about">頁面1</RouterLink>
        <RouterLink to="/firstPage">頁面2</RouterLink>
        <RouterLink to="/chatRoom">聊天室</RouterLink>
      </el-aside>
      <el-container>
        <el-header>
          <RouterLink to="/about">頁面1</RouterLink>
          <RouterLink to="/firstPage">頁面2</RouterLink>
          <RouterLink to="/chatRoom">聊天室</RouterLink>
          <RouterLink to="/" @click="isLoggedIn = false">登出</RouterLink>
        </el-header>
        <el-main>
            <router-view
                v-slot="{ Component }">
              <component :is="Component"/>
            </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import HomePage from "@/views/HomePage.vue";
import {ref} from "vue";

const isLoggedIn = ref(false)

</script>
<style scoped>
.aside-nav {
  display: flex;
  flex-direction: column;
  padding: 1rem;
}

.aside-nav a {
  margin-bottom: 0.5rem;
}

.common-layout {
  display: flex;
  justify-content: center;
  align-items: center;
}

</style>
