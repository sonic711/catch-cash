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
			log.info("[AsyncPool:{}] poolSize={}, active={}, core={}, max={}, largest={}, queueSize={}, taskCount={}, completed={}",//
					name,//
					tp.getPoolSize(),//
					tp.getActiveCount(),//
					tp.getCorePoolSize(),//
					tp.getMaximumPoolSize(),//
					tp.getLargestPoolSize(),//
					tp.getQueue().size(),//
					tp.getTaskCount(),//
					tp.getCompletedTaskCount());
		}
	}
}
