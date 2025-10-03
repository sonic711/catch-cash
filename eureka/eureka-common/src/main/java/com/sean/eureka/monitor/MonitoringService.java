package com.sean.eureka.monitor;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * ====================================================================== <br>
 * </p>
 * <ul>
 * <li><strong>磁碟使用情況</strong> - 監控磁碟空間使用狀態</li>
 * <li><strong>執行緒池狀態</strong> - 追蹤應用程式執行緒池的運行狀況</li>
 * <li><strong>資料庫連接池</strong> - 監控 HikariCP 和 JDBC 連接池狀態</li>
 * <li><strong>HTTP 請求統計</strong> - 記錄客戶端和伺服器端的 HTTP 請求狀況</li>
 * <li><strong>JVM 效能指標</strong> - 包含記憶體、垃圾回收、執行緒、類別載入等</li>
 * <li><strong>系統資源監控</strong> - CPU 使用率、系統記憶體、負載平均等</li>
 * <li><strong>應用程式指標</strong> - Log4j2 事件、排程任務、Tomcat 會話等</li>
 * </ul>
 * <p>
 * 所有監控資料都會透過 Log4j2 記錄到 "SELF-MONITORING" 日誌類別中，
 * 並以格式化的繁體中文訊息呈現，便於運維人員監控系統健康狀態。
 * </p>
 * <p>
 * 監控頻率可透過 Spring Boot 的 {@code self.monitoring.fixedRate} 配置項目調整，
 * 預設每 60 秒執行一次監控作業。
 * </p>
 * ======================================================================
 */
@Service
public class MonitoringService {

	/**
	 * 專用於系統監控的日誌記錄器
	 */
	private static final Logger log = LogManager.getLogger("SELF-MONITORING");

	/**
	 * 監控排程開關，用於控制是否執行定期監控任務
	 */
	@Value("${self.monitoring.schedule:true}")
	private boolean monitoringScheduleEnabled;

	/**
	 * Micrometer 指標讀取器，用於獲取系統各項指標數據
	 */
	private final MicrometerMetricsReader metricsReader;

	/**
	 * 百分比數值格式化器，用於格式化百分比數據（例如：CPU 使用率）
	 */
	private final DecimalFormat percentFormat = new DecimalFormat("0.00%");

	/**
	 * 數值格式化器，用於格式化一般數值數據並添加千分位逗號
	 */
	private final DecimalFormat numberFormat = new DecimalFormat("#,###.##");

	/**
	 * 時間格式化器，用於格式化時間戳為可讀的日期時間字串
	 */
	private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 建構函式，初始化監控服務
	 *
	 * @param metricsReader Micrometer 指標讀取器實例，由 Spring 依賴注入提供
	 */
	public MonitoringService(MicrometerMetricsReader metricsReader) {
		this.metricsReader = metricsReader;
	}

	/**
	 * 定期執行系統指標監控和記錄作業
	 * <p>
	 * 這是主要的監控方法，會依序執行以下監控作業：
	 * </p>
	 * <ol>
	 * <li>磁碟使用情況監控</li>
	 * <li>執行緒池狀態檢查</li>
	 * <li>資料庫連接池狀態（HikariCP 和 JDBC）</li>
	 * <li>HTTP 請求統計</li>
	 * <li>JVM 相關指標（記憶體、垃圾回收、執行緒等）</li>
	 * <li>系統資源使用狀況</li>
	 * <li>應用程式特定指標（Log4j2、排程任務、Tomcat）</li>
	 * </ol>
	 * <p>
	 * 執行頻率由 Spring Boot 配置屬性 {@code self.monitoring.fixedRate} 控制，
	 * 預設為每 60000 毫秒（60 秒）執行一次。
	 * </p>
	 *
	 * @see org.springframework.scheduling.annotation.Scheduled
	 */
	@Scheduled(fixedRateString = "${self.monitoring.fixedRate:60000}")
	public void printSystemMetrics() {
		// 檢查監控排程是否啟用，如果停用則直接返回
		if (!monitoringScheduleEnabled) {
			return;
		}

		log.info("========== 系統監控指標 ==========");

		// 磁碟使用情況
		printDiskMetrics();

		// 執行緒池狀態
		printExecutorMetrics();

		// HikariCP 連接池狀態
		printHikariCPMetrics();

		// HTTP 請求統計
		printHttpMetrics();

		// JDBC 連接池
		printJdbcMetrics();

		// JVM 緩衝區
		printJvmBufferMetrics();

		// JVM 類別載入
		printJvmClassMetrics();

		// JVM 編譯時間
		printJvmCompilationMetrics();

		// JVM 記憶體管理
		printJvmMemoryMetrics();

		// JVM 垃圾回收
		printJvmGCMetrics();

		// JVM 執行緒
		printJvmThreadMetrics();

		// Log4j2 事件
		printLog4j2Metrics();

		// 系統程序
		printProcessMetrics();

		// 系統資源
		printSystemResourceMetrics();

		// 排程任務
		printTaskMetrics();

		// Tomcat 會話
		printTomcatMetrics();

		log.info("================================");
	}

	/**
	 * 記錄磁碟使用情況相關指標
	 * <p>
	 * 監控並記錄系統磁碟的使用狀況，包括：
	 * </p>
	 * <ul>
	 * <li>磁碟可用空間 - 目前可使用的磁碟空間大小</li>
	 * <li>磁碟總容量 - 磁碟的總容量大小</li>
	 * </ul>
	 * 所有數值都會透過 {@link #formatBytes(double)} 方法格式化為易讀的單位。
	 */
	private void printDiskMetrics() {
		metricsReader.getGaugeValue("disk.free").ifPresent(value -> log.info("[磁碟監控] 可用磁碟空間: {} (系統剩餘儲存空間)", formatBytes(value)));
		metricsReader.getGaugeValue("disk.total").ifPresent(value -> log.info("[磁碟監控] 磁碟總容量: {} (系統總儲存空間)", formatBytes(value)));
	}

	/**
	 * 記錄執行緒池狀態相關指標
	 * <p>
	 * 監控應用程式中執行緒池的運作狀況，包括：
	 * </p>
	 * <ul>
	 * <li>活躍執行緒數 - 目前正在執行任務的執行緒數量</li>
	 * <li>已完成任務數 - 執行緒池已完成的任務總數</li>
	 * <li>核心大小 - 執行緒池的核心執行緒數</li>
	 * <li>最大大小 - 執行緒池允許的最大執行緒數</li>
	 * <li>目前大小 - 執行緒池目前的執行緒總數</li>
	 * <li>佇列剩餘容量 - 任務佇列剩餘的容量</li>
	 * <li>佇列中任務數 - 目前在佇列中等待執行的任務數</li>
	 * </ul>
	 */
	private void printExecutorMetrics() {
		metricsReader.getValueWithTag("executor.active", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 目前活躍執行緒數: {}", name, numberFormat.format(value));
		});

		// TODO
		metricsReader.getValueWithTag("executor.completed", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 已完成任務數: {} 個 (累計處理完成的任務總數)", name, numberFormat.format(value));
		});

		metricsReader.getValueWithTag("executor.pool.core", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 核心執行緒數: {} 個 (基本常駐執行緒數量)", name, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("executor.pool.max", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 最大執行緒數: {} 個 (系統負載高峰時可達上限)", name, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("executor.pool.size", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 目前執行緒數: {} 個 (目前實際運行的執行緒總數)", name, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("executor.queue.remaining", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 佇列剩餘容量: {} 個 (還可容納的等待任務數)", name, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("executor.queued", "name").forEach((name, value) -> {
			log.info("[執行緒池] 執行緒池 {} 佇列中任務數: {} 個 (排隊等待處理的任務)", name, numberFormat.format(value));
		});
	}

	private void printHikariCPMetrics() {
		metricsReader.getGaugeValue("hikaricp.connections").ifPresent(value -> log.info("[資料庫連接池] HikariCP 總連接數: {} 個 (連接池中所有連接的總數)", numberFormat.format(value)));
		metricsReader.getTimerValue("hikaricp.connections.acquire")//
				.ifPresent(value -> {
					log.info("[資料庫連接池] HikariCP 獲取連接總次數: {} 次 (應用程式請求連接的總次數)", numberFormat.format(value.count()));
					log.info("[資料庫連接池] HikariCP 獲取連接累積耗時: {} ms (總等待時間，越低越好)", numberFormat.format(value.totalTime(TimeUnit.MILLISECONDS)));
					log.info("[資料庫連接池] HikariCP 獲取連接最長耗時: {} ms (單次最長等待時間)", numberFormat.format(value.max(TimeUnit.MILLISECONDS)));
				});
		metricsReader.getGaugeValue("hikaricp.connections.active").ifPresent(value -> log.info("[資料庫連接池] HikariCP 活躍連接數: {} 個 (正在執行SQL查詢的連接)", numberFormat.format(value)));
		metricsReader.getTimerValue("hikaricp.connections.creation")//
				.ifPresent(value -> {
					log.info("[資料庫連接池] HikariCP 建立新連線次數: {} 次 (實際建立的資料庫連接數)", numberFormat.format(value.count()));
					log.info("[資料庫連接池] HikariCP 建立新連線累積耗時: {} ms (建立連接總耗時)", numberFormat.format(value.totalTime(TimeUnit.MILLISECONDS)));
					log.info("[資料庫連接池] HikariCP 建立新連線最長耗時: {} ms (單次建立最長時間)", numberFormat.format(value.max(TimeUnit.MILLISECONDS)));
				});
		metricsReader.getGaugeValue("hikaricp.connections.idle").ifPresent(value -> log.info("[資料庫連接池] HikariCP 閒置連接數: {} 個 (空閒可用的連接數)", numberFormat.format(value)));
		metricsReader.getGaugeValue("hikaricp.connections.max").ifPresent(value -> log.info("[資料庫連接池] HikariCP 最大連接數: {} 個 (連接池容量上限)", numberFormat.format(value)));
		metricsReader.getGaugeValue("hikaricp.connections.min").ifPresent(value -> log.info("[資料庫連接池] HikariCP 最小連接數: {} 個 (連接池最低保持數量)", numberFormat.format(value)));
		metricsReader.getGaugeValue("hikaricp.connections.pending").ifPresent(value -> log.info("[資料庫連接池] HikariCP 等待連接數: {} 個 (排隊等待獲取連接的請求)", numberFormat.format(value)));
		metricsReader.getCounterValue("hikaricp.connections.timeout").ifPresent(value -> log.info("[資料庫連接池] HikariCP 連接逾時次數: {} 次 (獲取連接超時的失敗次數)", numberFormat.format(value.count())));
		metricsReader.getTimerValue("hikaricp.connections.usage")//
				.ifPresent(value -> {
					log.info("[資料庫連接池] HikariCP 連線使用總次數: {} 次 (連接被借用的總次數)", numberFormat.format(value.count()));
					log.info("[資料庫連接池] HikariCP 連線使用累積時間: {} ms (連接被使用的總時間)", numberFormat.format(value.totalTime(TimeUnit.MILLISECONDS)));
					log.info("[資料庫連接池] HikariCP 連線最長使用時間: {} ms (單次使用最長時間)", numberFormat.format(value.max(TimeUnit.MILLISECONDS)));
				});
	}

	private void printHttpMetrics() {
		metricsReader.getTimerValue("http.client.requests").ifPresent(value -> {
			log.info("[HTTP客戶端] 對外請求總數: {} 次 (應用程式發出的HTTP請求總計)", numberFormat.format(value.count()));
			log.info("[HTTP客戶端] 對外請求總耗時: {} 毫秒 (所有外部請求累積時間)", value.totalTime(TimeUnit.MILLISECONDS));
			log.info("[HTTP客戶端] 對外請求最長響應時間: {} 毫秒 (單次請求最長等待時間)", value.max(TimeUnit.MILLISECONDS));
		});
		metricsReader.getLongTaskTimerValue("http.client.requests.active")//
				.ifPresent(value -> {
					log.info("[HTTP客戶端] 進行中的對外請求數: {} 個 (正在等待回應的請求)", numberFormat.format(value.activeTasks()));
					log.info("[HTTP客戶端] 活躍請求累積時間: {} 毫秒 (當前請求已執行時間)", value.duration(TimeUnit.MILLISECONDS));
					log.info("[HTTP客戶端] 活躍請求最長執行時間: {} 毫秒 (當前最久的請求時間)", value.max(TimeUnit.MILLISECONDS));
				});
		// TODO
		metricsReader.getTimerValue("http.server.requests").ifPresent(value -> {
			log.info("[HTTP伺服器] 接收請求總數: {} 次 (客戶端向本服務發送的請求總計)", numberFormat.format(value.count()));
			log.info("[HTTP伺服器] 處理請求總耗時: {} 毫秒 (所有請求處理累積時間)", value.totalTime(TimeUnit.MILLISECONDS));
			log.info("[HTTP伺服器] 最長請求處理時間: {} 毫秒 (單次請求最長處理時間)", value.max(TimeUnit.MILLISECONDS));
		});
		// TODO
		metricsReader.getLongTaskTimerValue("http.server.requests.active")//
				.ifPresent(value -> {
					log.info("[HTTP伺服器] 處理中的請求數: {} 個 (正在處理的客戶端請求)", numberFormat.format(value.activeTasks()));
					log.info("[HTTP伺服器] 活躍請求累積處理時間: {} 毫秒 (當前請求已處理時間)", value.duration(TimeUnit.MILLISECONDS));
					log.info("[HTTP伺服器] 活躍請求最長處理時間: {} 毫秒 (當前處理最久的請求)", value.max(TimeUnit.MILLISECONDS));
				});
	}

	private void printJdbcMetrics() {
		metricsReader.getGaugeValue("jdbc.connections.max").ifPresent(value -> log.info("[JDBC連接池] 最大連接數: {} 個 (JDBC層級的連接池上限)", numberFormat.format(value)));
		metricsReader.getGaugeValue("jdbc.connections.min").ifPresent(value -> log.info("[JDBC連接池] 最小連接數: {} 個 (JDBC層級的連接池下限)", numberFormat.format(value)));
	}

	private void printJvmBufferMetrics() {
		metricsReader.getGaugeValue("jvm.buffer.count").ifPresent(value -> log.info("[JVM緩衝區] 緩衝區數量: {} 個 (直接記憶體緩衝區總數)", numberFormat.format(value)));
		metricsReader.getGaugeValue("jvm.buffer.memory.used").ifPresent(value -> log.info("[JVM緩衝區] 已使用記憶體: {} (直接記憶體使用量)", formatBytes(value)));
		metricsReader.getGaugeValue("jvm.buffer.total.capacity").ifPresent(value -> log.info("[JVM緩衝區] 總容量: {} (直接記憶體總容量)", formatBytes(value)));
	}

	private void printJvmClassMetrics() {
		metricsReader.getGaugeValue("jvm.classes.loaded").ifPresent(value -> log.info("[JVM類別載入] 已載入類別數: {} 個 (目前載入到記憶體的類別總數)", numberFormat.format(value)));
		metricsReader.getFunctionCounterValue("jvm.classes.unloaded").ifPresent(value -> log.info("[JVM類別載入] 已卸載類別數: {} 個 (垃圾回收清理的類別數量)", numberFormat.format(value.count())));
	}

	private void printJvmCompilationMetrics() {
		metricsReader.getFunctionCounterValue("jvm.compilation.time")//
				.ifPresent(value -> {
					log.info("[JVM編譯器] JIT編譯次數: {} 次 (即時編譯器優化代碼的次數)", numberFormat.format(value.count()));
				});
	}

	private void printJvmMemoryMetrics() {
		metricsReader.getGaugeValue("jvm.memory.committed").ifPresent(value -> log.info("[JVM記憶體] 已提交記憶體: {} (作業系統保證可用的記憶體)", formatBytes(value)));
		metricsReader.getValueWithTag("jvm.memory.max", "id")//
				.forEach((key, value) -> log.info("[JVM記憶體] {}最大記憶體: {} (JVM可使用的記憶體上限)", key, formatBytes(value)));
		metricsReader.getValueWithTag("jvm.memory.used", "area")//
				.forEach((key, value) -> log.info("[JVM記憶體] 已使用{}記憶體: {} (目前實際使用的記憶體)", key, formatBytes(value)));
		metricsReader.getGaugeValue("jvm.memory.usage.after.gc").ifPresent(value -> log.info("[JVM記憶體] GC後記憶體使用率: {} (垃圾回收後的記憶體使用比例)", percentFormat.format(value)));
	}

	private void printJvmGCMetrics() {
		metricsReader.getGaugeValue("jvm.gc.live.data.size").ifPresent(value -> log.info("[JVM垃圾回收] 存活資料大小: {} (老年代中存活物件占用的記憶體)", formatBytes(value)));
		metricsReader.getGaugeValue("jvm.gc.max.data.size").ifPresent(value -> log.info("[JVM垃圾回收] 最大資料大小: {} (老年代記憶體池的最大容量)", formatBytes(value)));
		metricsReader.getCounterValue("jvm.gc.memory.allocated").ifPresent(value -> log.info("[JVM垃圾回收] 年輕代分配記憶體總量: {} (新建物件累計分配的記憶體)", formatBytes(value.count())));
		metricsReader.getCounterValue("jvm.gc.memory.promoted").ifPresent(value -> log.info("[JVM垃圾回收] 老年代晉升記憶體總量: {} (從年輕代晉升到老年代的記憶體)", formatBytes(value.count())));
		metricsReader.getGaugeValue("jvm.gc.overhead").ifPresent(value -> log.info("[JVM垃圾回收] GC開銷比例: {} (垃圾回收占用CPU時間的比例)", percentFormat.format(value)));
		metricsReader.getTimerValue("jvm.gc.pause")//
				.ifPresent(value -> {
					log.info("[JVM垃圾回收] GC暫停次數: {} 次 (垃圾回收執行的總次數)", numberFormat.format(value.count()));
					log.info("[JVM垃圾回收] GC暫停總時長: {} ms (所有GC暫停的累積時間)", numberFormat.format(value.totalTime(TimeUnit.MILLISECONDS)));
					log.info("[JVM垃圾回收] GC最長暫停時間: {} ms (單次GC的最長暫停時間)", numberFormat.format(value.max(TimeUnit.MILLISECONDS)));
				});
	}

	private void printJvmThreadMetrics() {
		metricsReader.getGaugeValue("jvm.threads.daemon").ifPresent(value -> log.info("[JVM執行緒] 背景執行緒數: {} 個 (系統背景服務執行緒)", numberFormat.format(value)));
		metricsReader.getGaugeValue("jvm.threads.live").ifPresent(value -> log.info("[JVM執行緒] 活躍執行緒數: {} 個 (目前運行中的執行緒總數)", numberFormat.format(value)));
		metricsReader.getGaugeValue("jvm.threads.peak").ifPresent(value -> log.info("[JVM執行緒] 執行緒峰值: {} 個 (歷史最高執行緒數量)", numberFormat.format(value)));
		metricsReader.getFunctionCounterValue("jvm.threads.started").ifPresent(value -> log.info("[JVM執行緒] 累計啟動執行緒數: {} 個 (程序啟動以來建立的執行緒總數)", numberFormat.format(value.count())));
		metricsReader.getGaugeValue("jvm.threads.states").ifPresent(value -> log.info("[JVM執行緒] 執行緒狀態統計: {} 個 (各種狀態的執行緒數量)", numberFormat.format(value)));
	}

	private void printLog4j2Metrics() {
		metricsReader.getCounterValue("log4j2.events").ifPresent(value -> log.info("[日誌系統] Log4j2事件總數: {} 個 (系統產生的日誌事件累計數量)", numberFormat.format(value.count())));
	}

	private void printProcessMetrics() {
		metricsReader.getFunctionCounterValue("process.cpu.time").ifPresent(value -> log.info("[系統程序] CPU使用時間: {} (程序累計占用CPU的時間)", formatDuration(value.count() / 1000000000)));
		metricsReader.getGaugeValue("process.cpu.usage").ifPresent(value -> log.info("[系統程序] CPU使用率: {} (程序當前CPU占用比例)", percentFormat.format(value)));
		metricsReader.getGaugeValue("process.files.max").ifPresent(value -> log.info("[系統程序] 最大檔案描述符數: {} 個 (程序可開啟的檔案上限)", numberFormat.format(value)));
		metricsReader.getGaugeValue("process.files.open").ifPresent(value -> log.info("[系統程序] 已開啟檔案描述符數: {} 個 (程序目前開啟的檔案數量)", numberFormat.format(value)));
		metricsReader.getGaugeValue("process.start.time").ifPresent(value -> log.info("[系統程序] 程序啟動時間: {} (應用程式開始運行的時間)", formatTimestamp(value)));
		metricsReader.getGaugeValue("process.uptime").ifPresent(value -> log.info("[系統程序] 程序運行時間: {} (應用程式持續運行的時長)", formatDuration(value)));
	}

	private void printSystemResourceMetrics() {
		metricsReader.getGaugeValue("system.cpu.count").ifPresent(value -> log.info("[系統資源] CPU核心數: {} 個 (伺服器可用的處理器核心總數)", numberFormat.format(value)));
		metricsReader.getGaugeValue("system.cpu.usage").ifPresent(value -> log.info("[系統資源] CPU使用率: {} (整個系統的CPU占用比例)", percentFormat.format(value)));
		metricsReader.getGaugeValue("system.load.average.1m").ifPresent(value -> log.info("[系統資源] 1分鐘負載平均: {} (系統負載指標，低於核心數為正常)", numberFormat.format(value)));
		metricsReader.getGaugeValue("system.memory.available").ifPresent(value -> log.info("[系統資源] 可用記憶體: {} (系統剩餘可用的記憶體)", formatBytes(value)));
		metricsReader.getGaugeValue("system.memory.total").ifPresent(value -> log.info("[系統資源] 總記憶體: {} (伺服器安裝的記憶體總容量)", formatBytes(value)));
	}

	private void printTaskMetrics() {
		metricsReader.getTimerValue("tasks.scheduled.execution")//
				.ifPresent(value -> {
					log.info("[排程任務] 執行總次數: {} 次 (定時任務執行的累計次數)", numberFormat.format(value.count()));
					log.info("[排程任務] 執行總時長: {} 秒 (所有定時任務累積執行時間)", value.totalTime(TimeUnit.SECONDS));
					log.info("[排程任務] 最長執行時間: {} 秒 (單次任務執行的最長時間)", value.max(TimeUnit.SECONDS));
				});
		metricsReader.getLongTaskTimerValue("tasks.scheduled.execution.active")//
				.ifPresent(value -> {
					log.info("[排程任務] 執行中任務數量: {} 個 (目前正在執行的定時任務)", numberFormat.format(value.activeTasks()));
					log.info("[排程任務] 活躍任務持續時間: {} 秒 (當前任務已執行時間)", value.duration(TimeUnit.SECONDS));
					log.info("[排程任務] 活躍任務最長時間: {} 秒 (當前執行最久的任務時間)", value.max(TimeUnit.SECONDS));
				});
	}

	private void printTomcatMetrics() {

		metricsReader.getValueWithTag("tomcat.connections.config.max", "name").forEach((protocol, value) -> {
			log.info("[Web容器] Tomcat協定 {} 最大連線數: {} 個 (連線池配置的上限)", protocol, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.connections.current", "name").forEach((protocol, value) -> {
			log.info("[Web容器] Tomcat協定 {} 目前連線數: {} 個 (目前建立中的連線數量)", protocol, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.connections.keepalive.current", "name").forEach((protocol, value) -> {
			log.info("[Web容器] Tomcat協定 {} Keep-Alive連線數: {} 個 (持續保持的連線數量)", protocol, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.global.error", "name").forEach((processor, value) -> {
			log.info("[Web容器] Tomcat處理器 {} 錯誤次數: {} 次 (全域請求處理錯誤總數)", processor, numberFormat.format(value));
		});
		metricsReader.getFunctionTimerValue("tomcat.global.request", "name").forEach((key, timer) -> {
			log.info("[Web容器] Tomcat處理器 {} 已處理請求數: {} 次 (全域連線累計處理數)", key, numberFormat.format(timer.count()));
			log.info("[Web容器] Tomcat處理器 {} 總處理請時間: {} 秒 (全域連線累計秒數)", key, numberFormat.format(timer.totalTime(TimeUnit.SECONDS)));
		});
		metricsReader.getValueWithTag("tomcat.global.request.max", "name").forEach((processor, value) -> {
			log.info("[Web容器] Tomcat處理器 {} 最長請求時間: {} 秒 (單次請求最長處理時間)", processor, numberFormat.format(value));
		});

		metricsReader.getValueWithTag("tomcat.servlet.error", "name").forEach((servlet, value) -> {
			log.info("[Web容器] Servlet {} 錯誤次數: {} 次 (該Servlet回應錯誤的總數)", servlet, numberFormat.format(value));
		});
		metricsReader.getFunctionTimerValue("tomcat.servlet.request", "name").forEach((key, timer) -> {
			log.info("[Web容器] Servlet {} 請求次數: {} 次 (該Servlet累計處理的請求數)", key, numberFormat.format(timer.count()));
			log.info("[Web容器] Servlet {} 請求總時間: {} 秒 (該Servlet累計處理的秒數)", key, numberFormat.format(timer.totalTime(TimeUnit.SECONDS)));
		});
		metricsReader.getValueWithTag("tomcat.servlet.request.max", "name").forEach((servlet, value) -> {
			log.info("[Web容器] Servlet {} 最長請求時間: {} 秒 (單次請求的最長處理時間)", servlet, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.threads.busy", "name").forEach((executor, value) -> {
			log.info("[Web容器] Tomcat執行器 {} 忙碌執行緒: {} 個 (目前正在處理請求的執行緒)", executor, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.threads.config.max", "name").forEach((executor, value) -> {
			log.info("[Web容器] Tomcat執行器 {} 最大執行緒數: {} 個 (配置可用的執行緒上限)", executor, numberFormat.format(value));
		});
		metricsReader.getValueWithTag("tomcat.threads.current", "name").forEach((executor, value) -> {
			log.info("[Web容器] Tomcat執行器 {} 目前執行緒數: {} 個 (當前建立的執行緒總數)", executor, numberFormat.format(value));
		});
		metricsReader.getGaugeValue("tomcat.sessions.active.current").ifPresent(value -> log.info("[Web容器] Tomcat活躍會話數: {} 個 (目前在線用戶的會話數)", numberFormat.format(value)));
	}

	/**
	 * 格式化位元組數值為人類可讀的儲存單位格式
	 * <p>
	 * 將原始的位元組數值轉換為適當的儲存單位，並加上千分位逗號分隔符，
	 * 使數值更易於閱讀和理解。轉換規則如下：
	 * </p>
	 * <ul>
	 * <li>≥ 1 GB：顯示為 GB 單位</li>
	 * <li>≥ 1 MB：顯示為 MB 單位</li>
	 * <li>≥ 1 KB：顯示為 KB 單位</li>
	 * <li>< 1 KB：顯示為 Bytes 單位</li>
	 * </ul>
	 *
	 * @param bytes 原始位元組數值
	 * @return 格式化後的儲存容量字串，包含適當的單位
	 */
	private String formatBytes(double bytes) {
		if (bytes >= 1024 * 1024 * 1024) {
			return numberFormat.format(bytes / (1024 * 1024 * 1024)) + " GB";
		} else if (bytes >= 1024 * 1024) {
			return numberFormat.format(bytes / (1024 * 1024)) + " MB";
		} else if (bytes >= 1024) {
			return numberFormat.format(bytes / 1024) + " KB";
		} else {
			return numberFormat.format(bytes) + " Bytes";
		}
	}

	/**
	 * 格式化時間戳為台灣時區的日期時間字串
	 * <p>
	 * 將時間戳數值轉換為台灣時區 (Asia/Taipei) 的可讀日期時間格式。
	 * 此方法會智慧判斷輸入的時間戳格式：
	 * </p>
	 * <ul>
	 * <li>如果數值大於 4102444800L (2100-01-01)，視為毫秒時間戳</li>
	 * <li>否則視為秒級時間戳</li>
	 * </ul>
	 * <p>
	 * 輸出格式為：yyyy-MM-dd HH:mm:ss
	 * </p>
	 *
	 * @param timestamp 時間戳數值（秒或毫秒）
	 * @return 格式化後的日期時間字串，如果轉換失敗則回傳原始數值的字串表示
	 */
	private String formatTimestamp(double timestamp) {
		try {
			Instant instant;
			// Spring Boot metrics typically provide timestamps in seconds
			// If the timestamp is too large to be seconds (greater than year 2100), treat as milliseconds
			if (timestamp > 4102444800L) { // 2100-01-01 00:00:00 UTC in seconds
				instant = Instant.ofEpochMilli((long) timestamp);
			} else {
				instant = Instant.ofEpochSecond((long) timestamp);
			}

			// Use Taiwan timezone explicitly
			ZoneId taiwanZone = ZoneId.of("Asia/Taipei");
			LocalDateTime dateTime = LocalDateTime.ofInstant(instant, taiwanZone);
			return dateTime.format(timeFormat);
		} catch (Exception e) {
			return numberFormat.format(timestamp);
		}
	}

	/**
	 * 格式化秒數為人類可讀的時間持續格式
	 * <p>
	 * 將秒數轉換為包含天、小時、分鐘、秒的易讀格式。
	 * 此方法會智慧地省略為零的單位，但秒數永遠會顯示。
	 * </p>
	 * <p>
	 * 格式化範例：
	 * </p>
	 * <ul>
	 * <li>90061 秒 → "1天 1小時 1分鐘 1秒"</li>
	 * <li>3661 秒 → "1小時 1分鐘 1秒"</li>
	 * <li>61 秒 → "1分鐘 1秒"</li>
	 * <li>30 秒 → "30秒"</li>
	 * </ul>
	 *
	 * @param seconds 總秒數
	 * @return 格式化後的時間持續字串，使用繁體中文時間單位
	 */
	private String formatDuration(double seconds) {
		long totalSeconds = (long) seconds;
		long days = totalSeconds / (24 * 3600);
		long hours = (totalSeconds % (24 * 3600)) / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long remainingSeconds = totalSeconds % 60;

		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append(days).append("天 ");
		if (hours > 0)
			sb.append(hours).append("小時 ");
		if (minutes > 0)
			sb.append(minutes).append("分鐘 ");
		sb.append(remainingSeconds).append("秒");

		return sb.toString();
	}
}

