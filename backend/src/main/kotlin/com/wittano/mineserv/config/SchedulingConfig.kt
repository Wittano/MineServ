package com.wittano.mineserv.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
@EnableScheduling
class SchedulingConfig : SchedulingConfigurer {
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val threadScheduler = ThreadPoolTaskScheduler()
        threadScheduler.poolSize = 4
        threadScheduler.setThreadNamePrefix("mineserv-task-")
        threadScheduler.initialize()

        taskRegistrar.setTaskScheduler(threadScheduler)
    }
}