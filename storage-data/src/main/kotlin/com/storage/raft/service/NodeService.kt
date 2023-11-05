package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeAdapter
import com.storage.raft.dto.VoteRequest
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
        node.initialize(lastTermChangedAt)
        log.info { "Node initialized. nodeType: ${node.nodeType}, nodeMeta: ${node.nodeMeta}, lastTermChangedAt: ${lastTermChangedAt}" }
    }

    fun heartbeat() {
        if (!node.isLeader()) {
            return
        }
        nodeAdapter.heartbeat(node.nodeType, getOtherNodeMetas())
    }

    fun saveNodeMeta(nodeMetas: Set<NodeMeta>) {
        val others = nodeMetas.filter { !node.eqNodeMeta(it) }.toSet()
        nodeRepository.saveNodeMetas(others)
    }

    fun maybeTryElection(now: LocalDateTime) {
        if (!node.isElectionTimeout(now)) {
            return
        }
        log.info { "Election timeout. Try election. ${node.nodeMeta}" }
        node.tryElection()

        val request = VoteRequest(node.nodeMeta, node.nodeTerm.term)
        nodeAdapter.requestVote(request, getOtherNodeMetas())
    }

    private fun getOtherNodeMetas(): Set<NodeMeta> {
        return nodeRepository.findAllNodeMetas()
    }

    fun getNodeMetaData(): NodeMeta {
        return node.nodeMeta
    }

}