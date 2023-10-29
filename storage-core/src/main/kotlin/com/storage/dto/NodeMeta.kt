package com.storage.dto

data class NodeMeta(
    val host: String,
    val port: Int,
) {
    val url: String
        get() = "http://${host}:${port}"
}
