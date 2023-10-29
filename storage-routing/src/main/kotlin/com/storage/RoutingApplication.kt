package com.storage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class RoutingApplication

fun main(args: Array<String>) {
    runApplication<RoutingApplication>(*args)
}
