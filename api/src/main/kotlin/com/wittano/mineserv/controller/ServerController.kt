package com.wittano.mineserv.controller

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.components.exceptions.ServerNotFoundException
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.request.ServerRequest
import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.data.views.DefaultView
import com.wittano.mineserv.interfaces.Mapper
import com.wittano.mineserv.interfaces.service.ServerService
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.batch.item.validator.Validator
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/api/server")
class ServerController(
    private val manager: ServerService,
    private val validator: Validator<Server>,
    private val repo: ServerRepository,
    private val mapper: Mapper<ServerRequest, Server>
) {

    private fun managerAction(id: Long, action: (Server) -> Mono<Server>): Mono<Response<Server>> =
        action(repo.findById(id).orElseThrow {
            throw ServerNotFoundException("Server with $id id isn't exist in database")
        }).map {
            Response(it)
        }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(DefaultView.Companion.External::class)
    fun create(@RequestBody server: ServerRequest): Mono<Response<Server>> =
        mapper.map(server).toMono().doOnNext {
            validator.validate(it)
        }.doOnSuccess {
            manager.create(it)
        }.map {
            Response(it)
        }

    @DeleteMapping("/{id}")
    @JsonView(DefaultView.Companion.External::class)
    fun delete(@PathVariable("id") id: Long): Mono<Response<Server>> =
        managerAction(id) {
            manager.delete(it)
        }

    @PutMapping("/stop/{id}")
    @JsonView(DefaultView.Companion.External::class)
    fun stop(@PathVariable("id") id: Long): Mono<Response<Server>> =
        managerAction(id) {
            manager.stop(it)
        }

    @PutMapping("/start/{id}")
    @JsonView(DefaultView.Companion.External::class)
    fun start(@PathVariable("id") id: Long): Mono<Response<Server>> =
        managerAction(id) {
            manager.start(it)
        }
}