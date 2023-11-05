package com.storage.raft.action

import com.storage.dto.NodeMeta
import com.storage.raft.domain.NodeType
import com.storage.raft.dto.HeartBeatResult
import com.storage.raft.dto.HeartbeatRequest
import com.storage.raft.dto.VoteRequest
import com.storage.raft.dto.VoteResponse
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

    private fun apiCall(nodeMeta: NodeMeta, request: HeartbeatRequest): HeartBeatResult {
        return try {
            logger.info { "send heartbeat to: ${nodeMeta}" }
            nodeAdapterRestTemplate.postForEntity("${nodeMeta.url}/api/v1/node-infra/heartbeat", request, Unit::class.java)
            logger.info { "success heartbeat to: ${nodeMeta}" }
            HeartBeatResult.success(nodeMeta)
        } catch (e: Throwable) {
            logger.info(e) { "heartbeat failed. nodeMeta: ${nodeMeta}" }
            HeartBeatResult.fail(nodeMeta)
        }
    }

    override fun requestVote(request: VoteRequest, nodeMetas: Set<NodeMeta>): Set<VoteResponse> {
        return nodeMetas.map {
            CompletableFuture.supplyAsync(
                { apiCall(it, request) },
                nodeRequestExecutor
            )
        }.joinAll().toSet()
    }

    private fun apiCall(nodeMeta: NodeMeta, request: VoteRequest): VoteResponse {
        return try {
            logger.info { "request vote to: ${nodeMeta}" }
            nodeAdapterRestTemplate.postForEntity("${nodeMeta.url}/api/v1/node-infra/request-vote", request, Unit::class.java)
            logger.info { "success request vote to: ${nodeMeta}" }
            VoteResponse.success(nodeMeta)
        } catch (e: Throwable) {
            logger.info(e) { "request vote failed. nodeMeta: ${nodeMeta}" }
            VoteResponse.fail(nodeMeta)
        }
    }

    private fun <T> List<CompletableFuture<T>>.joinAll(): List<T> {
        return this.map { it.join() }
    }
}
