package com.wittano.mineserv.controller

import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.service.miencraft.PropertiesService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping("/api/properties")
class PropertiesController(
    private val propertiesService: PropertiesService
) {

    @GetMapping("/{id}")
    fun getProperties(@PathVariable("id") id: Long): Mono<Response<Properties>> =
        propertiesService.getProperties(id).toMono().map {
            Response(it)
        }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateProperties(@PathVariable("id") id: Long, @RequestBody properties: Properties) {
        propertiesService.updateProperties(id, properties)
    }
}