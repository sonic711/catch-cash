package com.sean.eureka.monitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * ====================================================================== <br>
 * 程式代號: MicrometerMetricsReader.java<br>
 * 程式說明: Micrometer 指標讀取器，用於從 MeterRegistry 中讀取各種類型的指標數據<br>
 * 這個組件負責從 Micrometer 的 MeterRegistry 中讀取各種類型的指標數據，
 * 包括計數器(Counter)、量規(Gauge)等，並提供便利的方法來獲取指標值。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 * <li>讀取計數器類型的指標數據</li>
 * <li>讀取量規類型的指標數據</li>
 * <li>支援帶標籤的指標查詢</li>
 * <li>提供安全的 Optional 回傳值，避免空指標異常</li>
 * ======================================================================
 */
@Component
public class MicrometerMetricsReader {

	/**
	 * Micrometer 指標註冊表，用於查詢各種指標數據。
	 * 由 Spring 容器管理，負責儲存和提供應用程式中的所有指標。
	 */
	private final MeterRegistry meterRegistry;

	/**
	 * 建構函式，透過依賴注入初始化指標讀取器。
	 *
	 * @param meterRegistry Micrometer 指標註冊表實例，由 Spring 自動注入。
	 */
	@Autowired
	public MicrometerMetricsReader(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	/**
	 * 查詢指定名稱的計時器（Timer）指標。
	 * Timer 用於測量短時間操作的耗時和頻率，例如 API 請求的執行時間。
	 *
	 * @param name 計時器指標的名稱，不可為空。
	 * @return 包含計時器實例的 Optional，若指標不存在則回傳空的 Optional。
	 */
	public Optional<Timer> getTimerValue(String name) {
		Timer timer = meterRegistry.find(name).timer();
		return timer != null ? Optional.of(timer) : Optional.empty();
	}

	/**
	 * 查詢指定名稱和標籤鍵的函數計時器（FunctionTimer）指標。
	 * FunctionTimer 用於測量由函數提供的計時數據，通常用於外部系統的計時。
	 *
	 * @param name   函數計時器指標的名稱，不可為空。
	 * @param tagKey 標籤鍵，用於過濾特定的標籤值。
	 * @return 包含標籤值與對應函數計時器的 Map，若無符合條件則回傳空 Map。
	 */
	public Map<String, FunctionTimer> getFunctionTimerValue(String name, String tagKey) {
		Map<String, FunctionTimer> result = new HashMap<>();
		meterRegistry.find(name).functionTimers().forEach(timer -> {
			String tagValue = timer.getId().getTag(tagKey);
			if (tagValue != null) {
				result.put(tagValue, timer);
			}
		});
		return result;
	}

	/**
	 * 查詢指定名稱的長任務計時器（LongTaskTimer）指標。
	 * LongTaskTimer 用於測量長時間運行的任務，例如後台作業的執行時間。
	 *
	 * @param name 長任務計時器指標的名稱，不可為空。
	 * @return 包含長任務計時器實例的 Optional，若指標不存在則回傳空的 Optional。
	 */
	public Optional<LongTaskTimer> getLongTaskTimerValue(String name) {
		LongTaskTimer longTaskTimer = meterRegistry.find(name).longTaskTimer();
		return longTaskTimer != null ? Optional.of(longTaskTimer) : Optional.empty();
	}

	/**
	 * 查詢指定名稱的計數器（Counter）指標。
	 * Counter 用於記錄單調遞增的計數值，例如錯誤次數或請求次數。
	 *
	 * @param name 計數器指標的名稱，不可為空。
	 * @return 包含計數器實例的 Optional，若指標不存在則回傳空的 Optional。
	 */
	public Optional<Counter> getCounterValue(String name) {
		Counter counter = meterRegistry.find(name).counter();
		return counter != null ? Optional.of(counter) : Optional.empty();
	}

	public Optional<FunctionCounter> getFunctionCounterValue(String name) {
		FunctionCounter functionCounter = meterRegistry.find(name).functionCounter();
		return functionCounter != null ? Optional.of(functionCounter) : Optional.empty();
	}

	/**
	 * 獲取指定名稱的量規（Gauge）數值。
	 * 量規是一種可增可減的指標類型，用於表示某個時刻的瞬時值，
	 * 例如目前記憶體使用量、活躍連接數、佇列長度等即時狀態數據。
	 *
	 * @param name 量規指標的名稱，不可為空。
	 * @return 包含量規數值的 Optional，若指標不存在則回傳空的 Optional。
	 */
	public Optional<Double> getGaugeValue(String name) {
		Gauge gauge = meterRegistry.find(name).gauge();
		return gauge != null ? Optional.of(gauge.value()) : Optional.empty();
	}

	/**
	 * 查詢指定名稱和標籤鍵的指標數值，並返回標籤值與數值的映射。
	 * 支援 Gauge、Counter 和 FunctionCounter 類型的指標。
	 *
	 * @param name   指標的名稱，不可為空。
	 * @param tagKey 標籤鍵，用於過濾特定的標籤值。
	 * @return 包含標籤值與對應數值的 Map，若無符合條件則回傳空 Map。
	 */
	public Map<String, Double> getValueWithTag(String name, String tagKey) {
		Map<String, Double> result = new HashMap<>();

		// 使用一次查找，處理所有類型的指標
		Collection<Meter> meters = meterRegistry.find(name).meters();

		meters.forEach(meter -> {
			String tagValue = meter.getId().getTag(tagKey);
			if (tagValue != null) {
				Double value = extractValue(meter);
				if (value != null) {
					result.put(tagValue, value);
				}
			}
		});

		return result;
	}

	/**
	 * 從指定的指標（Meter）中提取數值。
	 * 根據指標類型（Gauge、Counter 或 FunctionCounter）返回相應的數值。
	 *
	 * @param meter 要提取數值的指標實例。
	 * @return 提取的數值，若無法提取則返回 null。
	 */
	private Double extractValue(Meter meter) {
		// 直接從 meter 的 measurements 中取值
		for (Measurement measurement : meter.measure()) {
			return measurement.getValue(); // 返回第一個測量值
		}
		return null;
	}
}

