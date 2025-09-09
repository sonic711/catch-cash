package com.sean.eureka.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.LongTaskTimer;
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
	 * Micrometer 指標註冊表，用於查詢各種指標數據
	 */
	private final MeterRegistry meterRegistry;

	/**
	 * 建構函式，透過依賴注入初始化指標讀取器
	 *
	 * @param meterRegistry Micrometer 指標註冊表實例，由 Spring 自動注入
	 */
	@Autowired
	public MicrometerMetricsReader(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	public Optional<Timer> getTimerValue(String name) {
		Timer timer = meterRegistry.find(name).timer();
		return timer != null ? Optional.of(timer) : Optional.empty();
	}

	public Optional<LongTaskTimer> getLongTaskTimerValue(String name) {
		LongTaskTimer longTaskTimer = meterRegistry.find(name).longTaskTimer();
		return longTaskTimer != null ? Optional.of(longTaskTimer) : Optional.empty();
	}

	public Optional<Counter> getCounterValue(String name) {
		Counter counter = meterRegistry.find(name).counter();
		return counter != null ? Optional.of(counter) : Optional.empty();
	}

	/**
	 * 獲取指定名稱的量規(Gauge)數值
	 * <p>
	 * 量規是一種可增可減的指標類型，用於表示某個時刻的瞬時值，
	 * 例如目前記憶體使用量、活躍連接數、佇列長度等即時狀態數據。
	 * </p>
	 *
	 * @param name 指標名稱，不可為空
	 * @return 包含量規數值的 Optional，如果指標不存在則回傳空的 Optional
	 */
	public Optional<Double> getGaugeValue(String name) {
		Gauge gauge = meterRegistry.find(name).gauge();
		return gauge != null ? Optional.of(gauge.value()) : Optional.empty();
	}

	/**
	 * 獲取指定名稱和標籤的指標數值
	 * <p>
	 * 支援帶標籤的指標查詢，標籤可以用來區分同一指標的不同維度，
	 * 例如按照不同的服務名稱、狀態碼、或地區來分組統計數據。
	 * 此方法會先嘗試尋找量規類型的指標，如果找不到則嘗試尋找計數器類型。
	 * </p>
	 *
	 * @param name     指標名稱，不可為空
	 * @param tagKey   標籤鍵，用於指定標籤的名稱
	 * @param tagValue 標籤值，用於指定標籤的具體數值
	 * @return 包含指標數值的 Optional，如果指標不存在則回傳空的 Optional
	 */
	public Optional<Double> getValueWithTag(String name, String tagKey, String tagValue) {
		Gauge gauge = meterRegistry.find(name).tag(tagKey, tagValue).gauge();
		if (gauge != null) {
			return Optional.of(gauge.value());
		}

		Counter counter = meterRegistry.find(name).tag(tagKey, tagValue).counter();
		if (counter != null) {
			return Optional.of(counter.count());
		}

		return Optional.empty();
	}

	public Map<String, Double> getValueWithTag(String name, String tagKey) {
		Map<String, Double> result = new HashMap<>();

		// 查找所有匹配 name 和 tagKey 的 gauge
		meterRegistry.find(name).gauges().forEach(gauge -> {
			String tagValue = gauge.getId().getTag(tagKey);
			if (tagValue != null) {
				result.put(tagValue, gauge.value());
			}
		});

		// 查找所有匹配 name 和 tagKey 的 counter
		meterRegistry.find(name).counters().forEach(counter -> {
			String tagValue = counter.getId().getTag(tagKey);
			if (tagValue != null) {
				result.put(tagValue, counter.count());
			}
		});

		return result;
	}
}



