package com.storage.config

import com.storage.raft.action.NodeAdapter
import com.storage.raft.repository.NodeRepository
import com.storage.raft.service.NodeGenerator
import com.storage.raft.service.NodeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class NodeConfig(
    @Value("\${server.port}") private val port: Int,
    private val nodeProperty: NodeProperty,
    private val nodeAdapter: NodeAdapter,
    private val nodeRepository: NodeRepository,
) : ApplicationListener<ApplicationStartedEvent> {

    @Bean
    fun node(): NodeService {
        val node = NodeGenerator.generate(
            host = nodeProperty.host,
            port = port,
            nodeType = nodeProperty.nodeType,
            electionTimeoutMinMs = nodeProperty.electionTimeout.minMs,
            electionTimeoutMaxMs = nodeProperty.electionTimeout.maxMs,
        )

        return NodeService(
            nodeAdapter = nodeAdapter,
            nodeRepository = nodeRepository,
            node = node,
        )
    }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        node().initialize(LocalDateTime.now())
    }
}