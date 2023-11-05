package com.storage.dto

enum class NodeType {
    LEADER,
    FOLLOWER,
    CANDIDATE,
    ;

    fun tryElection(): NodeType {
        check(this == FOLLOWER) { "${this} 상태는 tryElection 불가능" }
        return CANDIDATE
    }

    fun checkVoteable() {
        check(this == CANDIDATE) { "${this} 상태는 vote 될 수 없음" }
    }

    fun promote(): NodeType {
        check(this == CANDIDATE) { "${this} 상태는 promote 불가능" }
        return CANDIDATE
    }
}