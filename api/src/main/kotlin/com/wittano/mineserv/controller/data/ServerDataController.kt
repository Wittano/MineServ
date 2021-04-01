package com.wittano.mineserv.controller.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.views.DefaultView
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.security.Principal

@RestController
@RequestMapping("/api/server")
class ServerDataController(
    private val repo: ServerRepository
) {
    @GetMapping("")
    @JsonView(DefaultView.Companion.External::class)
    fun getServers(principal: Principal): Flux<Server> = Flux.fromIterable(
        repo.findAllByOwnerName(principal.name)
    )
}