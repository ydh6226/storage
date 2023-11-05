package com.storage.raft.domain

enum class NodeType {
    LEADER,
    FOLLOWER,
    CANDIDATE,
    ;

    fun tryElection(): NodeType {
        check(this == FOLLOWER) { "${this} 상태는 tryElection() 불가능" }
        return CANDIDATE
    }

    fun checkVoteable() {
        check(this == CANDIDATE) { "${this} vote 불가능" }
    }
}