package com.storage.raft.schedule

import com.storage.dto.DataNodeAliveRequest
import com.storage.raft.service.NodeService
import com.storage.service.DataNodeHealthCheckService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HealthCheckSchedule(
    private val nodeService: NodeService,
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
        val request = DataNodeAliveRequest(nodeService.getNodeMetaData())
        val response = dataNodeHealthCheckService.aliveDataNode(request)

        log.info { "${response}" }
        nodeService.saveNodeMeta(response.nodeMetas)
    }
}