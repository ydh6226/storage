package com.storage.controller

import com.storage.dto.DataNodeAliveRequest
import com.storage.dto.DataNodeAliveResponse
import com.storage.dto.NodeMeta
import com.storage.dto.NodeType
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class DataNodeHealthCheckController {

    private val log = KotlinLogging.logger {}

    private var leaderNodeMeta: NodeMeta? = null
    private val nodeMetaToCreatedAt = mutableMapOf<NodeMeta, LocalDateTime>()

    @PostMapping("/data-node/alive")
    fun alive(@RequestBody request: DataNodeAliveRequest): DataNodeAliveResponse {
        saveNodeMeta(request.nodeMeta)
        if (request.nodeType == NodeType.LEADER) {
            log.info { "leader election. ${request.nodeMeta}" }
            leaderNodeMeta = request.nodeMeta
        }
        return DataNodeAliveResponse(nodeMetaToCreatedAt.keys)
    }

    private fun saveNodeMeta(nodeMeta: NodeMeta) {
        log.info { "데이터 노드 등록 ${nodeMeta}" }
        nodeMetaToCreatedAt[nodeMeta] = LocalDateTime.now()
    }

    // TODO: 삭제는 하면 안되나?
    @Scheduled(fixedRate = 1000)
    fun deleteUnhealthyNodeMeta() {
        val unhealthyNode = nodeMetaToCreatedAt.filterValues { createdAt ->
            createdAt < LocalDateTime.now().minusSeconds(30)
        }.keys

        unhealthyNode.forEach {
            log.info { "unhealthy 데이터 노드 삭제. ${it}" }
            nodeMetaToCreatedAt.remove(it)
        }
    }
}