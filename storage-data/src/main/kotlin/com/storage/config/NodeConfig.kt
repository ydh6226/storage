package com.storage.config

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeActionService
import com.storage.raft.domain.NodeType
import com.storage.raft.repository.NodeRepository
import com.storage.raft.service.Node
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NodeConfig(
    @Value("\${node.host}") private val host: String,
    @Value("\${server.port}") private val port: Int,
    @Value("\${node.type:FOLLOWER}") private val nodeType: NodeType,
) {

    @Bean
    fun node(
        nodeActionService: NodeActionService,
        nodeRepository: NodeRepository,
    ): Node {
        return Node(
            nodeActionService = nodeActionService,
            nodeRepository = nodeRepository,
            nodeType = nodeType,
            nodeMeta = NodeMeta(host, port),
        )
    }
}