package com.storage.config

import com.storage.dto.NodeType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "node")
data class NodeProperty(
    val host: String,
    val nodeType: NodeType = NodeType.FOLLOWER,
    val electionTimeout: ElectionTimeout,
) {

    data class ElectionTimeout(
        val minMs: Long,
        val maxMs: Long,
    )

}

