package com.storage.raft.action

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import com.storage.raft.dto.HeartBeatResult
import com.storage.raft.dto.VoteRequest
import com.storage.raft.dto.VoteResponse

interface NodeAdapter {

    fun heartbeat(nodeType: NodeType, nodeMetas: Set<NodeMeta>): Set<HeartBeatResult>

    fun requestVote(request: VoteRequest, nodeMetas: Set<NodeMeta>): Set<VoteResponse>
}