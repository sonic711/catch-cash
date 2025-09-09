package com.sean.batch.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 統一的非同步執行緒池配置，可支持多個命名的執行器。
 * 使用 @Async("<poolName>") 來選擇一個執行緒池。
 */
@Slf4j
@Configuration
@EnableAsync
@EnableConfigurationProperties(ThreadPoolsProperties.class)
public class AsyncThreadPoolConfig implements AsyncConfigurer {

	private final ThreadPoolsProperties props;
	private final ConfigurableListableBeanFactory beanFactory;

	public AsyncThreadPoolConfig(ThreadPoolsProperties props, ConfigurableListableBeanFactory beanFactory) {
		this.props = props;
		this.beanFactory = beanFactory;
	}

	@Bean
	public Map<String, ThreadPoolTaskExecutor> asyncExecutors(ObjectProvider<List<TaskExecutorCustomizer>> customizersProvider) {
		List<TaskExecutorCustomizer> customizers = customizersProvider.getIfAvailable();
		if (customizers == null)
			customizers = Collections.emptyList();
		final List<TaskExecutorCustomizer> finalCustomizers = customizers;

		Map<String, ThreadPoolTaskExecutor> map = new HashMap<>();
		if (props.getExecutors().isEmpty()) {
			// 如果沒有配置，則使用預設執行緒池
			ThreadPoolsProperties.ExecutorProperties def = new ThreadPoolsProperties.ExecutorProperties();
			def.setCorePoolSize(2);
			def.setMaxPoolSize(4);
			def.setQueueCapacity(100);
			props.getExecutors().put("default", def);
		}
		props.getExecutors().forEach((name, cfg) -> {
			// 如果已存在同名的 Bean（例如，備用的顯式 @Bean），則重用它
			if (beanFactory.containsBean(name)) {
				try {
					ThreadPoolTaskExecutor existing = (ThreadPoolTaskExecutor) beanFactory.getBean(name);
					map.put(name, existing);
					return; // 跳過創建/註冊另一個 bean
				} catch (Exception e) {
					// 如果類型不匹配，則繼續創建新的
				}
			}
			ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
			ex.setThreadNamePrefix(cfg.getThreadNamePrefix() != null ? cfg.getThreadNamePrefix() : name + "-");
			ex.setCorePoolSize(cfg.getCorePoolSize());
			ex.setMaxPoolSize(cfg.getMaxPoolSize());
			ex.setQueueCapacity(cfg.getQueueCapacity());
			ex.setKeepAliveSeconds(cfg.getKeepAliveSeconds());
			ex.setAllowCoreThreadTimeOut(cfg.isAllowCoreThreadTimeout());
			ex.setWaitForTasksToCompleteOnShutdown(true);
			ex.setRejectedExecutionHandler(resolvePolicy(cfg.getRejectedPolicy()));
			for (TaskExecutorCustomizer c : finalCustomizers) {
				try {
					c.customize(name, ex, cfg);
				} catch (Exception e) {
					log.warn("Customizer error for {}", name, e);
				}
			}
			ex.initialize();
			// 將每個執行器註冊為獨立的 bean，以便 @Async(name) 可以找到它
			beanFactory.registerSingleton(name, ex);
			map.put(name, ex);
		});
		return map;
	}

	private ThreadPoolExecutor.AbortPolicy resolveAbort() {return new ThreadPoolExecutor.AbortPolicy();}

	private ThreadPoolExecutor.CallerRunsPolicy resolveCallerRuns() {return new ThreadPoolExecutor.CallerRunsPolicy();}

	private ThreadPoolExecutor.DiscardPolicy resolveDiscard() {return new ThreadPoolExecutor.DiscardPolicy();}

	private ThreadPoolExecutor.DiscardOldestPolicy resolveDiscardOldest() {return new ThreadPoolExecutor.DiscardOldestPolicy();}

	private java.util.concurrent.RejectedExecutionHandler resolvePolicy(String policy) {
		if (policy == null)
			return resolveAbort();
		return switch (policy.toUpperCase()) {
			case "CALLER_RUNS" -> resolveCallerRuns();
			case "DISCARD" -> resolveDiscard();
			case "DISCARD_OLDEST" -> resolveDiscardOldest();
			default -> resolveAbort();
		};
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> log.error("Uncaught async error in {} with params {}", method, params, ex);
	}

	// 如果 @Async 沒有指定名稱，則提供預設執行器
    @Override
    public java.util.concurrent.Executor getAsyncExecutor() {
        // 如果存在名為 "default" 的 bean，則優先使用
        try {
            return (ThreadPoolTaskExecutor) beanFactory.getBean("default");
        } catch (Exception ignored) {
            return null; // 退回使用 SimpleAsyncTaskExecutor
        }
    }

    // 備用的顯式 Beans，以確保 @Async("batch") / @Async("common") 總能被解析
    @Bean(name = "batch")
    @ConditionalOnMissingBean(name = "batch")
    public ThreadPoolTaskExecutor batchExecutor() {
        return createExecutor("batch");
    }

    @Bean(name = "common")
    @ConditionalOnMissingBean(name = "common")
    public ThreadPoolTaskExecutor commonExecutor() {
        return createExecutor("common");
    }

    private ThreadPoolTaskExecutor createExecutor(String name) {
        ThreadPoolsProperties.ExecutorProperties cfg = props.getExecutors().get(name);
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        if (cfg == null) {
            ex.setThreadNamePrefix(name + "-");
            ex.setCorePoolSize(2);
            ex.setMaxPoolSize(4);
            ex.setQueueCapacity(100);
            ex.setKeepAliveSeconds(60);
            ex.setAllowCoreThreadTimeOut(false);
        } else {
            ex.setThreadNamePrefix(cfg.getThreadNamePrefix() != null ? cfg.getThreadNamePrefix() : name + "-");
            ex.setCorePoolSize(cfg.getCorePoolSize());
            ex.setMaxPoolSize(cfg.getMaxPoolSize());
            ex.setQueueCapacity(cfg.getQueueCapacity());
            ex.setKeepAliveSeconds(cfg.getKeepAliveSeconds());
            ex.setAllowCoreThreadTimeOut(cfg.isAllowCoreThreadTimeout());
            ex.setRejectedExecutionHandler(resolvePolicy(cfg.getRejectedPolicy()));
        }
        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.initialize();
        return ex;
    }

    public interface TaskExecutorCustomizer {
        void customize(String name, ThreadPoolTaskExecutor executor, ThreadPoolsProperties.ExecutorProperties cfg);
    }
}
