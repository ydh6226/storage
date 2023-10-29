package com.storage.raft.schedule

import com.storage.raft.service.Node
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NodeHeartBeatSchedule(
    private val node: Node,
) {

    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    fun heartbeat() {
        node.heartbeat()
    }
}