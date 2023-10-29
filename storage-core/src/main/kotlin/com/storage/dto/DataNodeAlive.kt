package com.storage.dto

data class DataNodeAliveRequest(
    val nodeMeta: NodeMeta,
)

data class DataNodeAliveResponse(
    val nodeMetas: Set<NodeMeta>,
)
