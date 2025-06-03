<template>
  <el-container class="architecture-container">
    <el-header>
      <el-divider><h1 class="title">主機架構圖</h1></el-divider>
    </el-header>
    <el-main>
      <el-row :gutter="40">
        <!-- 左側：使用者瀏覽器與 Jenkins UI -->
        <el-col :span="8" class="left-column">
          <h2 style="text-align: center;">User Side</h2>
          <el-card class="component-card">
            <h3>
              <el-icon>
                <User/>
              </el-icon>
              使用者瀏覽器
            </h3>
            <p>透過瀏覽器訪問 Jenkins UI 進行建置操作</p>
          </el-card>
          <br>
          <el-card class="component-card">
            <h3>
              <el-icon>
                <Monitor/>
              </el-icon>
              Jenkins UI
            </h3>
            <p>提供建置與管理介面</p>
          </el-card>
        </el-col>
        <!-- 中間：Git Server -->
        <el-col :span="8" class="middle-column">
          <h2 style="text-align: center;">Git Server</h2>
          <el-card class="component-card">
            <h3>
              <el-icon>
                <Connection/>
              </el-icon>
              Git Server (Github)
            </h3>
            <p>程式碼儲存庫，提供程式碼版本控制</p>
          </el-card>

        </el-col>
        <!-- 右側：Oracle Cloud -->
        <el-col :span="8" class="right-column">
          <div class="cloud-box">
            <h2 style="text-align: center;">Oracle Cloud</h2>
            <el-card class="component-card">
              <h3>
                <el-icon>
                  <Box/>
                </el-icon>
                Jenkins (Docker)
              </h3>
              <p>運行於 Docker 容器，負責程式碼建置與部署</p>
            </el-card>
            <br>
            <el-card class="component-card">
              <h3>
                <el-icon>
                  <Cpu/>
                </el-icon>
                Application
              </h3>
              <p>應用程式運行環境</p>
            </el-card>
            <br>
            <el-card class="component-card">
              <h3>
                <el-icon>
                  <Files/>
                </el-icon>
                MySQL (Docker)
              </h3>
              <p>運行於 Docker 容器，提供資料庫服務</p>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>
<script setup lang="ts">

</script>


<style scoped>

</style>
