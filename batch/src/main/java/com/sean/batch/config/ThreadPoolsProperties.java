package com.sean.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "batch.async")
public class ThreadPoolsProperties {

    private Map<String, ExecutorProperties> executors = new LinkedHashMap<>();

    // monitor interval seconds
    private int monitorIntervalSeconds = 60;

    public Map<String, ExecutorProperties> getExecutors() {
        return executors;
    }

    public void setExecutors(Map<String, ExecutorProperties> executors) {
        this.executors = executors;
    }

    public int getMonitorIntervalSeconds() {
        return monitorIntervalSeconds;
    }

    public void setMonitorIntervalSeconds(int monitorIntervalSeconds) {
        this.monitorIntervalSeconds = monitorIntervalSeconds;
    }

    public static class ExecutorProperties {
        private int corePoolSize = 2;
        private int maxPoolSize = 4;
        private int queueCapacity = 100;
        private int keepAliveSeconds = 60;
        private boolean allowCoreThreadTimeout = false;
        private String rejectedPolicy = "ABORT"; // ABORT, CALLER_RUNS, DISCARD, DISCARD_OLDEST
        private String threadNamePrefix;

        public int getCorePoolSize() { return corePoolSize; }
        public void setCorePoolSize(int corePoolSize) { this.corePoolSize = corePoolSize; }
        public int getMaxPoolSize() { return maxPoolSize; }
        public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }
        public int getQueueCapacity() { return queueCapacity; }
        public void setQueueCapacity(int queueCapacity) { this.queueCapacity = queueCapacity; }
        public int getKeepAliveSeconds() { return keepAliveSeconds; }
        public void setKeepAliveSeconds(int keepAliveSeconds) { this.keepAliveSeconds = keepAliveSeconds; }
        public boolean isAllowCoreThreadTimeout() { return allowCoreThreadTimeout; }
        public void setAllowCoreThreadTimeout(boolean allowCoreThreadTimeout) { this.allowCoreThreadTimeout = allowCoreThreadTimeout; }
        public String getRejectedPolicy() { return rejectedPolicy; }
        public void setRejectedPolicy(String rejectedPolicy) { this.rejectedPolicy = rejectedPolicy; }
        public String getThreadNamePrefix() { return threadNamePrefix; }
        public void setThreadNamePrefix(String threadNamePrefix) { this.threadNamePrefix = threadNamePrefix; }
    }
}
