# Eureka-Client Project

## 簡介

Eureka-Client 是一個用於服務發現與服務託管的插件。它可以用來發現其他服務。
透過Actuator & Eureka client實現

## 如何使用

```groovy
api project(":eureka:eureka-plugin")
```

Client端啟動類別上需要自行啟用排程功能，用於定期檢查服務健康狀態。

```java

@EnableScheduling
public class Application extends SpringBootServletInitializer {}
```

