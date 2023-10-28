package com.storage.infra

import com.storage.dto.DataNodeAliveRequest
import com.storage.dto.DataNodeAliveResponse
import com.storage.service.DataNodeHealthCheckService
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "http://localhost:8081/api/v1")
interface DataNodeHealthCheckServiceOpenFeignImpl : DataNodeHealthCheckService {

    @PostMapping("/data-node/alive")
    override fun aliveDataNode(request: DataNodeAliveRequest): DataNodeAliveResponse
}