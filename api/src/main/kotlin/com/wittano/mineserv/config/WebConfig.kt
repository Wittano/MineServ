package com.wittano.mineserv.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@Profile("dev", "docker", "prod")
class WebConfig : WebFluxConfigurer {

    @Value("\${project.client.domain}")
    private lateinit var domain: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins(domain)
            .allowedMethods("*")
            .allowCredentials(false)
            .maxAge(8000L)
            .exposedHeaders("Access-Control-Allow-Origin")
    }
}