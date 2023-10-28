package com.storage.config

import feign.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenFeignConfig {

    @Bean
    fun feignClient(): Client {
        return Client.Default(null, null)
    }
}