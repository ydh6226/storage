package com.storage.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class HelloController {

    private val log = KotlinLogging.logger {}

    @GetMapping("/foo")
    fun hello(): String {
        log.info { "hello" }
        return "ok"
    }
}