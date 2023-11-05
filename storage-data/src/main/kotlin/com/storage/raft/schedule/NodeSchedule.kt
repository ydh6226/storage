package com.storage.raft.schedule

import com.storage.raft.service.NodeService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NodeSchedule(
    private val nodeService: NodeService,
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = 5000)
    fun heartbeat() {
        try {
            nodeService.heartbeat()
        } catch (e: Throwable) {
            log.error(e) { "heartbeat schedule fail" }
        }
    }

    /**
     * initialDelay: 다른 노드 정보 받아온 후에 실행
     */
    @Scheduled(fixedDelay = 5, initialDelay = 150)
    fun maybeTryElection() {
        try {
            nodeService.maybeTryElection(LocalDateTime.now())
        } catch (e: Throwable) {
            log.error(e) { "maybeTryElection schedule fail" }
        }
    }
}