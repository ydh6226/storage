package com.storage.raft.dto

import com.storage.dto.NodeMeta

data class VoteRequest(
    val nodeMeta: NodeMeta,
    val term: Long,
)

data class VoteResponse(
    val nodeMeta: NodeMeta,
    val success: Boolean,
) {
    companion object {
        fun success(nodeMeta: NodeMeta): VoteResponse {
            return VoteResponse(nodeMeta, true)
        }

        fun fail(nodeMeta: NodeMeta): VoteResponse {
            return VoteResponse(nodeMeta, false)
        }
    }

}
