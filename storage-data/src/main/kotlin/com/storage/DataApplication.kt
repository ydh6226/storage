package com.storage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableFeignClients
@ConfigurationPropertiesScan
@SpringBootApplication
class DataApplication

fun main(args: Array<String>) {
    runApplication<DataApplication>(*args)
}
