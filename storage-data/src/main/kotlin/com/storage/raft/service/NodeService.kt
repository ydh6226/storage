package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeAdapter
import com.storage.raft.dto.VoteRequest
import com.storage.raft.dto.VoteResponse
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
        log.info { "Node initialized. nodeType: ${node.nodeType}, nodeMeta: ${node.nodeMeta}, electionTimeoutMs: ${node.nodeTerm.electionTimeoutMs}, lastTermChangedAt: ${lastTermChangedAt}" }
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
        val responses = nodeAdapter.requestVote(request, getOtherNodeMetas())

        responses.forEach {
            if (it.success) {
                node.vote()
            }
        }

        val quorum = getQuorum()
        if (!node.hasMajorityVotes(quorum)) {
            log.info { "Node not promoted. vote count: ${node.nodeTerm.voteCount}, quorum: ${quorum}" }
            return
        }

        log.info { "Node promoted. vote count: ${node.nodeTerm.voteCount}, quorum: ${quorum}" }
        node.promote()

        // TODO: send Append Entries
    }

    private fun getQuorum(): Int {
        return getOtherNodeMetas().size / 2 + 1
    }

    fun voteLeader(candidateNodeMeta: NodeMeta, term: Long): VoteResponse {
        return try {
            node.voteLeader(candidateNodeMeta, term)
            VoteResponse.success(node.nodeMeta, node.nodeTerm.term)
        } catch (e: Exception) {
            log.info { "vote leader fail. ${e.message}" }
            VoteResponse.fail(node.nodeMeta, node.nodeTerm.term, e.message)
        }
    }

    private fun getOtherNodeMetas(): Set<NodeMeta> {
        return nodeRepository.findAllNodeMetas()
    }

    fun getNodeMetaData(): NodeMeta {
        return node.nodeMeta
    }

}