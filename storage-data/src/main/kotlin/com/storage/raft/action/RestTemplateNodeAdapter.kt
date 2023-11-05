package com.storage.raft.action

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import com.storage.raft.dto.HeartBeatResult
import com.storage.raft.dto.HeartbeatRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

// TODO: OpenFeign
@Service
class RestTemplateNodeAdapter(
    private val nodeAdapterRestTemplate: RestTemplate,
    private val nodeRequestExecutor: Executor,
) : NodeAdapter {

    private val logger = KotlinLogging.logger {}

    override fun heartbeat(nodeType: NodeType, nodeMetas: Set<NodeMeta>): Set<HeartBeatResult> {
        return nodeMetas.map {
            val request = HeartbeatRequest("hello")
            CompletableFuture.supplyAsync(
                { apiCall(it, request) },
                nodeRequestExecutor
            )
        }.joinAll().toSet()
    }

    private fun apiCall(it: NodeMeta, request: HeartbeatRequest): HeartBeatResult {
        return try {
            logger.info { "send heartbeat to: ${it}" }
            nodeAdapterRestTemplate.postForEntity("${it.url}/api/v1/node-infra/heartbeat", request, Unit::class.java)
            logger.info { "success heartbeat to: ${it}" }
            HeartBeatResult.success(it)
        } catch (e: Throwable) {
            logger.info(e) { "heartbeat failed. nodeMeta: ${it}" }
            HeartBeatResult.fail(it)
        }
    }

    private fun List<CompletableFuture<HeartBeatResult>>.joinAll(): List<HeartBeatResult> {
        return this.map { it.join() }
    }
}
