package com.storage.dto

data class DataNodeAliveRequest(
    val nodeName: String,
)

data class DataNodeAliveResponse(
    val nodeNames: List<String>,
)
