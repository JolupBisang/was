package com.jolupbisang.demo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    /**
     * External API 호출용 스레드풀 (Whisper, AWS Step Function 등)
     * - I/O 바운드 작업이므로 풀 크기를 크게 설정
     * - 대기 시간이 길 수 있어 많은 동시 요청 처리 가능하도록 설정
     */
    @Bean(name = "externalApiExecutor")
    public TaskExecutor externalApiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(10);      // 기본 스레드 수
        executor.setMaxPoolSize(30);       // 최대 스레드 수
        executor.setQueueCapacity(100);    // 대기 큐 크기
        executor.setKeepAliveSeconds(60);  // 유휴 스레드 대기 시간
        
        executor.setThreadNamePrefix("external-api-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    /**
     * WebSocket 전송용 스레드풀
     * - 실시간 메시지 전송이므로 응답성이 중요
     * - 많은 동시 연결을 처리해야 함
     */
    @Bean(name = "websocketExecutor")
    public TaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(15);      // 기본 스레드 수
        executor.setMaxPoolSize(50);       // 최대 스레드 수
        executor.setQueueCapacity(200);    // 대기 큐 크기
        executor.setKeepAliveSeconds(60);  // 유휴 스레드 대기 시간
        
        executor.setThreadNamePrefix("websocket-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }

    /**
     * 일반 비동기 작업용 스레드풀
     * - CPU 바운드 또는 가벼운 작업
     * - 스케줄 관리, 간단한 이벤트 처리 등
     */
    @Bean(name = "AsyncTaskExecutor")
    public TaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(5);       // 기본 스레드 수
        executor.setMaxPoolSize(10);       // 최대 스레드 수
        executor.setQueueCapacity(50);     // 대기 큐 크기
        executor.setKeepAliveSeconds(60);  // 유휴 스레드 대기 시간
        
        executor.setThreadNamePrefix("async-task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
