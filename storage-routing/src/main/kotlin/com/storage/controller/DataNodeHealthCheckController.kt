package com.storage.controller

import com.storage.dto.DataNodeAliveRequest
import com.storage.dto.DataNodeAliveResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DataNodeHealthCheckController {

    private val log = KotlinLogging.logger {}

    private val nodeNames = mutableSetOf<String>()

    @PostMapping("/data-node/alive")
    fun alive(@RequestBody request: DataNodeAliveRequest): DataNodeAliveResponse {
        log.info { "Node: I'm alive! ${request}" }
        nodeNames.add(request.nodeName)
        return DataNodeAliveResponse(nodeNames.toList())
    }
}