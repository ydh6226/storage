package com.storage.config

import org.springframework.boot.task.TaskSchedulerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


/**
 * @Scheduled 관련 설정 참고 https://devel-repository.tistory.com/47
 */
@Configuration
class SchedulerConfig {

    @Bean
    fun taskSchedulerCustomizer(): TaskSchedulerCustomizer {
        return TaskSchedulerCustomizer { threadPoolTaskScheduler: ThreadPoolTaskScheduler ->
            threadPoolTaskScheduler.setPoolSize(5)
            threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true)
            threadPoolTaskScheduler.setAwaitTerminationSeconds(30)
            threadPoolTaskScheduler.setThreadNamePrefix("node-scheduling-")
        }
    }
}