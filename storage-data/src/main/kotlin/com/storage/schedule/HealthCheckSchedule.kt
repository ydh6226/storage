package com.storage.schedule

import com.storage.dto.DataNodeAliveRequest
import com.storage.service.DataNodeHealthCheckService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class HealthCheckSchedule(
    private val dataNodeHealthCheckService: DataNodeHealthCheckService,
) {

    private val log = KotlinLogging.logger {}

    private val uuid = UUID.randomUUID().toString()

    @Scheduled(fixedRate = 10000)
    fun dataNodeAlive() {
        try {
            invoke()
        } catch (e: Throwable) {
            log.error(e) { "request dataNodeAlive failed" }
        }
    }

    private fun invoke() {
        val request = DataNodeAliveRequest(uuid)
        val response = dataNodeHealthCheckService.aliveDataNode(request)
        log.info { "${response}" }
    }
}