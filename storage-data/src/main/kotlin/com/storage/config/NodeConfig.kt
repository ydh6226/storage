package com.storage.config

import com.storage.raft.action.NodeActionService
import com.storage.raft.repository.NodeRepository
import com.storage.raft.service.Node
import com.storage.raft.service.NodeCoreGenerator
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
    private val nodeActionService: NodeActionService,
    private val nodeRepository: NodeRepository,
) : ApplicationListener<ApplicationStartedEvent> {

    @Bean
    fun node(): Node {
        val nodeCore = NodeCoreGenerator.generate(
            host = nodeProperty.host,
            port = port,
            nodeType = nodeProperty.nodeType,
        )

        return Node(
            nodeActionService = nodeActionService,
            nodeRepository = nodeRepository,
            nodeCore = nodeCore,
        )
    }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        node().initialize(LocalDateTime.now())
    }
}