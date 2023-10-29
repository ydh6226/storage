package com.storage.raft.repository

import com.storage.dto.NodeMeta
import org.springframework.stereotype.Repository

@Repository
class NodeRepository {

    val nodeMetas: MutableSet<NodeMeta> = mutableSetOf()

    fun findAllNodeMetas(): Set<NodeMeta> {
        return nodeMetas
    }

    fun saveNodeMetas(nodeMeta: Set<NodeMeta>) {
        nodeMetas.addAll(nodeMeta)
    }
}