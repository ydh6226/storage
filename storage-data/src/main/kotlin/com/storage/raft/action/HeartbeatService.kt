package com.storage.raft.action

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import com.storage.raft.dto.HeartBeatResult

interface HeartbeatService {
    fun heartbeat(nodeType: NodeType, nodeMetas: Set<NodeMeta>): Set<HeartBeatResult>
}