package com.storage.raft.schedule

import com.storage.dto.DataNodeAliveRequest
import com.storage.raft.service.Node
import com.storage.service.DataNodeHealthCheckService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HealthCheckSchedule(
    private val node: Node,
    private val dataNodeHealthCheckService: DataNodeHealthCheckService,
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = 10000)
    fun dataNodeAlive() {
        try {
            invoke()
        } catch (e: Throwable) {
            log.error(e) { "request dataNodeAlive failed" }
        }
    }

    private fun invoke() {
        val request = DataNodeAliveRequest(node.nodeCore.nodeMeta)
        val response = dataNodeHealthCheckService.aliveDataNode(request)

        log.info { "${response}" }
        node.saveNodeMeta(response.nodeMetas)
    }
}