package com.storage.raft.controller

import com.storage.raft.dto.HeartbeatRequest
import com.storage.raft.dto.VoteRequest
import com.storage.raft.dto.VoteResponse
import com.storage.raft.service.NodeService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/node-infra")
@RestController
class NodeInfraController(
    private val nodeService: NodeService,
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/heartbeat")
    fun heartbeat(@RequestBody request: HeartbeatRequest) {
        logger.info { "Heartbeat from Leader. ${request}" }
        Thread.sleep(500)
    }

    @PostMapping("/request-vote")
    fun heartbeat(@RequestBody request: VoteRequest): VoteResponse {
        return nodeService.voteLeader(request.nodeMeta, request.term)
    }
}