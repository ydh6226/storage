package com.storage.raft.dto

import com.storage.dto.NodeMeta

data class HeartBeatResult(
    val nodeMeta: NodeMeta,
    val success: Boolean,
) {
    companion object {
        fun success(nodeMeta: NodeMeta): HeartBeatResult {
            return HeartBeatResult(nodeMeta, true)
        }

        fun fail(nodeMeta: NodeMeta): HeartBeatResult {
            return HeartBeatResult(nodeMeta, false)
        }
    }
}