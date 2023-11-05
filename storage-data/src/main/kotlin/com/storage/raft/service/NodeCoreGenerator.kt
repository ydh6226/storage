package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import kotlin.random.Random

object NodeCoreGenerator {

    fun generate(
        host: String,
        port: Int,
        nodeType: NodeType,
        electionTimeoutMs: Long = Random.nextLong(150, 300),
    ): NodeCore {
        return NodeCore(
            nodeMeta = NodeMeta(host, port),
            nodeType = nodeType,
            nodeTerm = NodeTerm(electionTimeoutMs = electionTimeoutMs)
        )
    }
}