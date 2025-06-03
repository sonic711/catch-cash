<template>
  <div class="app-container">
    <el-container class="main-container">
      <el-aside width="240px" class="aside-nav">
        <div class="logo-container">
          <RouterLink to="/home" class="logo-link">
            <h2 class="logo">Catch Cash</h2>
          </RouterLink>
        </div>
        <div class="nav-links">
          <RouterLink to="/about" class="nav-link">
            <el-icon>
              <Document/>
            </el-icon>
            <span>帳號管理</span>
          </RouterLink>
          <RouterLink to="/chatRoom" class="nav-link">
            <el-icon>
              <ChatDotRound/>
            </el-icon>
            <span>聊天室</span>
          </RouterLink>
        </div>
      </el-aside>
      <el-container>
        <el-header class="main-header">
          <div class="header-content">
            <div class="breadcrumb">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item>首頁</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentRoute }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
            <div class="user-actions">
              <el-dropdown>
                <span class="user-dropdown">
                  <el-avatar :size="32"
                             src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                  <span class="user-name">管理員</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item>個人資料</el-dropdown-item>
                    <el-dropdown-item>設定</el-dropdown-item>
                    <el-dropdown-item @click="logout">登出</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        <el-main class="content-main">
          <router-view
              v-slot="{ Component }">
            <el-card class="page-card">
              <component :is="Component"/>
            </el-card>
          </router-view>
        </el-main>
        <el-footer class="main-footer">
          © 2023 Catch Cash. All rights reserved.
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import {computed} from "vue";
import {useRoute, useRouter} from "vue-router";
import {ChatDotRound, Document} from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();

const currentRoute = computed(() => {
  const routeMap: Record<string, string> = {
    '/about': '帳號管理',
    '/firstPage': '頁面2',
    '/chatRoom': '聊天室'
  };
  return routeMap[route.path] || '首頁';
});

const logout = () => {
  localStorage.removeItem('X-Access-Token');
  router.push('/login');
};
</script>

<style>
/* Styles moved to main.css */
</style>
