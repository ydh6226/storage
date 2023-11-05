package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import kotlin.random.Random

object NodeGenerator {

    fun generate(
        host: String,
        port: Int,
        nodeType: NodeType,
        electionTimeoutMsMin: Long,
        electionTimeoutMsMax: Long,
    ): Node {
        val electionTimeoutMs = Random.nextLong(electionTimeoutMsMin, electionTimeoutMsMax)

        return Node(
            nodeMeta = NodeMeta(host, port),
            nodeType = nodeType,
            nodeTerm = NodeTerm(electionTimeoutMs = electionTimeoutMs)
        )
    }
}