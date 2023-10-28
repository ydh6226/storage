package com.storage.service

import com.storage.dto.DataNodeAliveRequest
import com.storage.dto.DataNodeAliveResponse

interface DataNodeHealthCheckService {
    fun aliveDataNode(request: DataNodeAliveRequest): DataNodeAliveResponse
}