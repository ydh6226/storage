package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.dto.NodeType
import java.time.LocalDateTime

// TODO: Lock, uuid
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
        nodeTerm.increaseTerm()
        voteLeader(nodeMeta, nodeTerm.term) // votes for itself
    }

    fun vote() {
        nodeType.checkVoteable()
        nodeTerm.vote()
    }

    fun hasMajorityVotes(quorum: Int): Boolean {
        return nodeTerm.hasMajorityVotes(quorum)
    }

    fun promote() {
        nodeType = nodeType.promote()
        nodeTerm.clearVoteCount()
    }

    // TODO: 리더 노드 정보 알고있어야 하나? / 뭐 검증해야함?
    fun voteLeader(candidateNodeMeta: NodeMeta, term: Long) {
        nodeTerm.voteLeader(candidateNodeMeta, term)
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
