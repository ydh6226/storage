package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeAdapter
import com.storage.raft.repository.NodeRepository
import mu.KotlinLogging
import java.time.LocalDateTime

class NodeService(
    private val nodeAdapter: NodeAdapter,
    private val nodeRepository: NodeRepository,
    private val node: Node,
) {

    private val log = KotlinLogging.logger {}

    fun initialize(lastTermChangedAt: LocalDateTime) {
        log.info { "Initialize node. nodeMeta: ${node.nodeMeta} lastTermChangedAt: ${lastTermChangedAt}" }
        node.initialize(lastTermChangedAt)
    }

    fun heartbeat() {
        if (!node.isLeader()) {
            return
        }
        val nodeMetas = nodeRepository.findAllNodeMetas()
        nodeAdapter.heartbeat(node.nodeType, nodeMetas)
    }

    fun saveNodeMeta(nodeMetas: Set<NodeMeta>) {
        val others = nodeMetas.filter { !node.eqNodeMeta(it) }.toSet()
        nodeRepository.saveNodeMetas(others)
    }

    fun maybeTryElection(now: LocalDateTime = LocalDateTime.now()) {
        if (!node.isElectionTimeout(now)) {
            return
        }
        log.info { "Election timeout. Try election. ${node.nodeMeta}" }
        node.tryElection()
        // TODO: requestVote
    }

    fun getNodeMetaData(): NodeMeta {
        return node.nodeMeta
    }

}