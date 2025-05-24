<template>
  <div class="app-container">
    <el-container class="main-container">
      <el-aside width="240px" class="aside-nav">
        <div class="logo-container">
          <h2 class="logo">Catch Cash</h2>
        </div>
        <div class="nav-links">
          <RouterLink to="/about" class="nav-link">
            <el-icon><Document /></el-icon>
            <span>頁面1</span>
          </RouterLink>
          <RouterLink to="/firstPage" class="nav-link">
            <el-icon><Grid /></el-icon>
            <span>頁面2</span>
          </RouterLink>
          <RouterLink to="/chatRoom" class="nav-link">
            <el-icon><ChatDotRound /></el-icon>
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
                  <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
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
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Document, Grid, ChatDotRound } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();

const currentRoute = computed(() => {
  const routeMap: Record<string, string> = {
    '/about': '頁面1',
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

<style scoped>
/* Main App Styles */
.app-container {
  height: 100vh;
  background-color: var(--background-color);
}

.main-container {
  height: 100%;
}

/* Sidebar Styles */
.aside-nav {
  background-color: var(--primary-color);
  color: white;
  height: 100%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
}

.logo-container {
  padding: 1.5rem;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  color: black;
  font-size: 1.5rem;
  margin: 0;
}

.nav-links {
  padding: 1rem 0;
  color: #000000; /* 設置文字顏色為黑色 */
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.8rem 1.5rem;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s;
  border-left: 3px solid transparent;
}

.nav-link:hover, .nav-link.router-link-active {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
  border-left-color: var(--secondary-color);
}

.nav-link .el-icon {
  margin-right: 0.8rem;
  font-size: 1.2rem;
}

.nav-links .nav-link {
  color: #222222; /* 設置文字顏色為黑色 */
}

.nav-links .nav-link el-icon {
  color: #222222; /* 設置圖標顏色為黑色 */
}

/* Header Styles */
.main-header {
  background-color: var(--light-color);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  padding: 0 1.5rem;
  height: 60px;
  display: flex;
  align-items: center;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-name {
  margin-left: 0.5rem;
  font-size: 0.9rem;
}

/* Main Content Styles */
.content-main {
  padding: 1.5rem;
  background-color: var(--background-color);
  overflow-y: auto;
}

.page-card {
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.08);
  border: 1px solid var(--border-color);
}

/* Footer Styles */
.main-footer {
  text-align: center;
  font-size: 0.8rem;
  color: var(--info-color);
  padding: 1rem;
  background-color: var(--light-color);
  border-top: 1px solid var(--border-color);
}
</style>
