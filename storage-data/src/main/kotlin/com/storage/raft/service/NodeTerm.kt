package com.storage.raft.service

import com.storage.dto.NodeMeta
import java.time.Duration
import java.time.LocalDateTime

data class NodeTerm(
    var term: Long = 0,
    var lastTermChangedAt: LocalDateTime = LocalDateTime.now(),
    var electionTimeoutMs: Long = 0, // TODO: 임기마다 바꿔야하나?
    var voteCount: Int = 0,
    val voteLeaderHistory: MutableMap<Long, NodeMeta> = mutableMapOf(), // key: term
) {

    fun isElectionTimeout(now: LocalDateTime): Boolean {
        val duration = Duration.between(lastTermChangedAt, now).toMillis()
        return duration > electionTimeoutMs
    }

    fun tryElection(nodeMeta: NodeMeta) {
        increaseTerm()
        voteMyself(nodeMeta)
    }

    private fun increaseTerm() {
        // TODO: change lastTermChangedAt
        term++
    }

    private fun voteMyself(nodeMeta: NodeMeta) {
        voted()
        voteLeader(nodeMeta, term)
    }

    fun voted() {
        voteCount++
    }

    fun hasMajorityVotes(quorum: Int): Boolean {
        return voteCount >= quorum
    }

    fun clearVoteCount() {
        voteCount = 0
    }

    fun voteLeader(candidateNodeMeta: NodeMeta, term: Long) {
        when (voteLeaderHistory[term]) {
            null -> voteLeaderHistory[term] = candidateNodeMeta
            candidateNodeMeta -> return
            else -> throw IllegalStateException("Already vote leader. vote node: $candidateNodeMeta, term: ${term}")
        }
    }

    fun resetLastTermChangedAt(lastTermChangedAt: LocalDateTime) {
        this.lastTermChangedAt = lastTermChangedAt
    }

}

