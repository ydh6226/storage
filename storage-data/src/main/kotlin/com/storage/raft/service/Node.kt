package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeAdapter
import com.storage.raft.repository.NodeRepository
import mu.KotlinLogging
import java.time.LocalDateTime

class Node(
    private val nodeAdapter: NodeAdapter,
    private val nodeRepository: NodeRepository,
    val nodeCore: NodeCore,
) {

    private val log = KotlinLogging.logger {}

    fun initialize(lastTermChangedAt: LocalDateTime) {
        log.info { "Initialize node. nodeMeta: ${nodeCore.nodeMeta} lastTermChangedAt: ${lastTermChangedAt}" }
        nodeCore.initialize(lastTermChangedAt)
    }

    fun heartbeat() {
        if (!nodeCore.isLeader()) {
            return
        }
        val nodeMetas = nodeRepository.findAllNodeMetas()
        nodeAdapter.heartbeat(nodeCore.nodeType, nodeMetas)
    }

    fun saveNodeMeta(nodeMetas: Set<NodeMeta>) {
        val others = nodeMetas.filter { !nodeCore.eqNodeMeta(it) }.toSet()
        nodeRepository.saveNodeMetas(others)
    }

    fun maybeTryElection(now: LocalDateTime = LocalDateTime.now()) {
        if (!nodeCore.isElectionTimeout(now)) {
            return
        }
        log.info { "Election timeout. Try election. ${nodeCore.nodeMeta}" }
        nodeCore.tryElection()
        // TODO: requestVote
    }

}