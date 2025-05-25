<template>
  <el-card class="chat-card">
    <template #header>
      <span>聊天室</span>
    </template>

    <div class="chat-messages">
      <div v-for="(msg, index) in messages" :key="index">
        {{ msg }}
      </div>
    </div>

    <div class="chat-input">
      <el-input
          v-model="input"
          placeholder="輸入訊息"
          @keyup.enter="sendMessage"
      />
      <el-button @click="sendMessage" type="primary">送出</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

const input = ref('')
const messages = ref<string[]>([])
let socket: WebSocket | null = null

onMounted(() => {
  socket = new WebSocket('ws://localhost:8082/channel/echo')

  socket.onopen = () => {
    console.log('✅ 連線已開啟')
    messages.value.push('連線成功')
  }

  socket.onmessage = (event) => {
    console.log('📨 收到訊息:', event.data)
    messages.value.push(`伺服器：${event.data}`)
  }

  socket.onerror = (event) => {
    console.error('❌ WebSocket 錯誤', event)
    messages.value.push('連線錯誤')
  }

  socket.onclose = (event) => {
    console.log(`🔌 連線關閉: code=${event.code}, reason=${event.reason}`)
    messages.value.push('連線已關閉')
  }
})

onBeforeUnmount(() => {
  if (socket) {
    socket.close()
  }
})

function sendMessage() {
  if (input.value.trim() && socket && socket.readyState === WebSocket.OPEN) {
    socket.send(input.value)
    messages.value.push(`你：${input.value}`)
    input.value = ''
  }
}
</script>

<style scoped>

.chat-messages {
  height: 300px;
  overflow-y: auto;
  padding: 10px;
  background-color: #f5f7fa;
  margin-bottom: 10px;
  border-radius: 4px;
  font-size: 14px;
}

.chat-input {
  display: flex;
  gap: 8px;
}
</style>
