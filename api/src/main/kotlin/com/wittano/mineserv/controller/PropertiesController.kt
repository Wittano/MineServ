package com.wittano.mineserv.controller

import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.service.miencraft.PropertiesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping("/api/properties")
class PropertiesController(
    private val properties: PropertiesService
) {

    @GetMapping("/{id}")
    fun getProperties(@PathVariable("id") id: Long): Mono<Response<Properties>> =
        properties.getProperties(id).toMono().map {
            Response(it)
        }
}