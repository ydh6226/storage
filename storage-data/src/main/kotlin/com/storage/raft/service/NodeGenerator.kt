package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.dto.NodeType
import kotlin.random.Random

object NodeGenerator {

    fun generate(
        host: String,
        port: Int,
        nodeType: NodeType,
        electionTimeoutMinMs: Long,
        electionTimeoutMaxMs: Long,
    ): Node {
        val electionTimeoutMs = Random.nextLong(electionTimeoutMinMs, electionTimeoutMaxMs)

        return Node(
            nodeMeta = NodeMeta(host, port),
            nodeType = nodeType,
            nodeTerm = NodeTerm(electionTimeoutMs = electionTimeoutMs)
        )
    }
}