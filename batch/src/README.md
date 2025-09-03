# Batch Module

## Async Thread Pool Unified Management
本功能提供多組執行緒池的統一控管，讓其他服務可以透過 `@Async("<poolName>")` 選擇要使用的執行緒池，並且會定期印出每個執行緒池的使用狀態。

### 如何設定
在 `application.properties` 或 yml 中配置。以下為 properties 的範例：

```
# 監控列印間隔秒數（預設60秒）
batch.async.monitor-interval-seconds=60

# 定義多組執行緒池（名稱: io、cpu、batch、common）
batch.async.executors.io.core-pool-size=4
batch.async.executors.io.max-pool-size=8
batch.async.executors.io.queue-capacity=200
batch.async.executors.io.thread-name-prefix=io-

batch.async.executors.cpu.core-pool-size=2
batch.async.executors.cpu.max-pool-size=4
batch.async.executors.cpu.queue-capacity=50
batch.async.executors.cpu.thread-name-prefix=cpu-

batch.async.executors.batch.core-pool-size=4
batch.async.executors.batch.max-pool-size=8
batch.async.executors.batch.queue-capacity=100
batch.async.executors.batch.thread-name-prefix=batch-

batch.async.executors.common.core-pool-size=2
batch.async.executors.common.max-pool-size=4
batch.async.executors.common.queue-capacity=100
batch.async.executors.common.thread-name-prefix=common-
```

屬性說明：
- core-pool-size：核心執行緒數
- max-pool-size：最大執行緒數
- queue-capacity：佇列長度
- keep-alive-seconds：非核心執行緒存活秒數（選填，預設60）
- allow-core-thread-timeout：是否允許核心執行緒逾時（選填，預設false）
- rejected-policy：拒絕策略（ABORT、CALLER_RUNS、DISCARD、DISCARD_OLDEST，選填，預設ABORT）
- thread-name-prefix：執行緒名前綴（建議設定）

若未設定任何 executors，系統會建立一組名為 `default` 的池（core=2, max=4, queue=100）。

### 如何使用
在你的 Service 方法上標註 `@Async` 並指定池名：

```java
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    @Async("io")
    public void doIoTask() {
        // IO 任務
    }

    @Async("cpu")
    public void doCpuTask() {
        // CPU 任務
    }

    // 若不指定名稱，會使用名為 default 的執行緒池（若存在）
    @Async
    public void defaultTask() {
    }
}
```

注意：專案已在 `AsyncThreadPoolConfig` 啟用 `@EnableAsync`，不需再次啟用。

### 執行緒池監控
系統會透過 `ThreadPoolMonitor` 以固定延遲的方式（依 `batch.async.monitor-interval-seconds` 設定）定期列印每個執行緒池的狀態，包含：
- poolSize, active, core, max, largest
- queueSize, taskCount, completed

Log 範例：
```
[AsyncPool:io] poolSize=2, active=1, core=4, max=8, largest=2, queueSize=0, taskCount=10, completed=9
```

### 範例 REST（原有備忘）
http://localhost:9090/catch-cash/process/start?services=BT001&services=BT002
{
"property1": "value1",
"property2": 123,
"property3": true
}

H2 Console: http://localhost:1111/batch/h2-console
