package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import java.time.LocalDateTime

// TODO: Lock
data class Node(
    var nodeMeta: NodeMeta,
    var nodeType: NodeType,
    var nodeTerm: NodeTerm,
) {
    // TODO: LeaderElection 실패할 때 처리
    fun isElectionTimeout(now: LocalDateTime): Boolean {
        return nodeType == NodeType.FOLLOWER && nodeTerm.isElectionTimeout(now)
    }

    fun tryElection() {
        nodeType = nodeType.tryElection()
        nodeTerm.tryElection()
    }

    fun vote() {
        nodeType.checkVoteable()
        nodeTerm.vote()
    }

    fun isLeader(): Boolean {
        return nodeType == NodeType.LEADER
    }

    fun eqNodeMeta(nodeMeta: NodeMeta): Boolean {
        return this.nodeMeta == nodeMeta
    }

    fun initialize(lastTermChangedAt: LocalDateTime) {
        nodeTerm.resetLastTermChangedAt(lastTermChangedAt)
    }
}
