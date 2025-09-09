package com.sean.batch.monitor;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ThreadPoolMonitor {

	private final Map<String, ThreadPoolTaskExecutor> executors;

	public ThreadPoolMonitor(Map<String, ThreadPoolTaskExecutor> executors) {
		this.executors = executors;
	}

	@Scheduled(fixedDelayString = "#{${batch.async.monitor-interval-seconds:60} * 1000}")
	public void logStatus() {
		if (executors.isEmpty())
			return;
		for (Map.Entry<String, ThreadPoolTaskExecutor> e : executors.entrySet()) {
			String name = e.getKey();
			ThreadPoolTaskExecutor ex = e.getValue();
			var tp = ex.getThreadPoolExecutor();
			log.info("[非同步執行緒池:{}] 池大小={}, 活躍數={}, 核心數={}, 最大數={}, 最大池大小={}, 隊列大小={}, 任務總數={}, 已完成={}",
					name,
					tp.getPoolSize(),
					tp.getActiveCount(),
					tp.getCorePoolSize(),
					tp.getMaximumPoolSize(),
					tp.getLargestPoolSize(),
					tp.getQueue().size(),
					tp.getTaskCount(),
					tp.getCompletedTaskCount());
		}
	}
}
