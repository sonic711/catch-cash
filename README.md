# Catch-Cash Project

## 簡介
Catch-Cash 是一個基於 Spring Boot 和 Gradle 的多模組專案，包含多個子模組，分別負責不同的功能。
```
                   ZZ  Cash是一隻2023年開始沉睡的小貓，
                  zz  雖然我會永遠記得，但還是把你放在這裡。
 　　　　　 /＞ _ _ フ
 　　　　　|   _　_ |
 　 　　　／`ミ＿꒳ ノ
 　　 　 /　　　 　 |
 　　　 /　 ヽ　　 ﾉ
 　 　 │　　|　|　|
 　／￣ | 　 |　|　|
 　| (￣ヽ＿ヽ )__)
 　＼二つ
```

## 專案結構
```
catch-cash
├── batch          # 負責批次處理相關功能
├── eureka         # 服務發現模組
│   ├── eureka-plugin  # Eureka 客戶端模組
│   ├── eureka-server  # Eureka 伺服器模組
├── socket         # WebSocket 模組
├── model          # 共享數據模型模組
├── web            # Web 服務端
├── web-vue        # 前端 Vue.js 專案
```
![專案架構](/etc/image/Architecture.jpg)
## 各模組介紹
### batch
負責批次處理的相關邏輯，可能涉及排程或數據處理工作。

### eureka
此模組包含兩個子模組：
- **eureka-plugin**：Eureka 客戶端，提供服務註冊與發現功能。
- **eureka-server**：Eureka 伺服器，用於管理微服務的註冊與發現。

### socket
提供 WebSocket 服務，用於與前端 Vue.js 互動，聊天室。

### model
存放共享的數據模型，供其他模組使用，確保數據結構的一致性。

### web
提供 Web API，基於 Spring Boot 3開發的後端服務。

### web-vue
前端專案，基於 Vue.js 開發，用於與後端 Web API 進行互動。

## 環境需求
- Java 17 或以上
- Gradle 8.7
- Node.js 18.17.0 或以上
---
© 2024 Catch-Cash. All rights reserved.
