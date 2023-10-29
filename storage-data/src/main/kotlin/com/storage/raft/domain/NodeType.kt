package com.storage.raft.domain

enum class NodeType {
    LEADER,
    FOLLOWER,
    CANDIDATE,
    ;
}