package com.storage.raft.schedule

import com.storage.raft.service.Node
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NodeSchedule(
    private val node: Node,
) {

    @Scheduled(fixedRate = 5000)
    fun heartbeat() {
        node.heartbeat()
    }

    /**
     * initialDelay: 다른 노드 정보 받아온 후에 실행
     */
    @Scheduled(fixedDelay = 5, initialDelay = 150)
    fun maybeTryElection() {
        node.maybeTryElection()
    }
}