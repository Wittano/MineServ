package com.wittano.mineserv.controller.data

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/server")
class ServerDataController(
    private val repo: ServerRepository
) {
    @GetMapping("")
    fun getServers(): Flux<Server> = Flux.fromIterable(
        repo.findAllByOwner(
            SecurityContextHolder
                .getContext()
                .authentication
                .principal as User
        )
    )
}