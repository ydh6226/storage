package com.storage.raft.dto

import com.storage.dto.NodeMeta

data class VoteRequest(
    val nodeMeta: NodeMeta,
    val term: Long,
)

data class VoteResponse(
    val nodeMeta: NodeMeta,
    val success: Boolean,
    val term: Long = 0, // TODO: 이거 왜 필요하지?
    val failReason: String = ""
) {
    companion object {
        private const val UNKNOWN_TERM = -1L
        private const val UNKNOWN_FAIL_REASON = "UNKNOWN"

        fun success(nodeMeta: NodeMeta, term: Long): VoteResponse {
            return VoteResponse(nodeMeta, true, term)
        }

        fun fail(
            nodeMeta: NodeMeta,
            term: Long = UNKNOWN_TERM,
            failReason: String? = UNKNOWN_FAIL_REASON,
        ): VoteResponse {
            return VoteResponse(nodeMeta, false, term, failReason ?: UNKNOWN_FAIL_REASON)
        }
    }

}
