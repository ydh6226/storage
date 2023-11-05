package com.storage.raft.service

import java.time.Duration
import java.time.LocalDateTime

data class NodeTerm(
    var term: Long = 0,
    var lastTermChangedAt: LocalDateTime = LocalDateTime.now(),
    var electionTimeoutMs: Long = 0, // TODO: 임기마다 바꿔야하나?
    var voteCount: Int = 0,
) {

    fun isElectionTimeout(now: LocalDateTime): Boolean {
        val duration = Duration.between(lastTermChangedAt, now).toMillis()
        return duration > electionTimeoutMs
    }

    fun tryElection() {
        increaseTerm()
        vote() // votes for itself
    }

    private fun increaseTerm() {
        term++
    }

    fun vote() {
        voteCount++
    }

    fun resetLastTermChangedAt(lastTermChangedAt: LocalDateTime) {
        this.lastTermChangedAt = lastTermChangedAt
    }

}

