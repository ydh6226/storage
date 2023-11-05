package com.storage.dto

data class DataNodeAliveRequest(
    val nodeMeta: NodeMeta,
    val nodeType: NodeType,
)

data class DataNodeAliveResponse(
    val nodeMetas: Set<NodeMeta>,
)
