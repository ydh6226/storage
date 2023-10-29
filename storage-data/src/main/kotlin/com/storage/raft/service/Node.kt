package com.storage.raft.service

import com.storage.dto.NodeMeta
import com.storage.raft.action.NodeActionService
import com.storage.raft.domain.NodeType
import com.storage.raft.repository.NodeRepository
import mu.KotlinLogging
import javax.annotation.PostConstruct

class Node(
    private val nodeActionService: NodeActionService,
    private val nodeRepository: NodeRepository,
    private var nodeType: NodeType,
    val nodeMeta: NodeMeta,
) {

    private val log = KotlinLogging.logger {}

    fun heartbeat() {
        if (nodeType != NodeType.LEADER) {
            return
        }

        val nodeMetas = nodeRepository.findAllNodeMetas()
        nodeActionService.heartbeat(nodeType, nodeMetas)
    }

    fun saveNodeMeta(nodeMetas: Set<NodeMeta>) {
        val others = nodeMetas.filter { it != nodeMeta }.toSet()
        nodeRepository.saveNodeMetas(others)
    }

    @PostConstruct
    fun logNodeMeta() {
        log.info { "I'm ${nodeType}. ${nodeMeta}" }
    }

}