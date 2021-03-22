package com.wittano.mineserv.service.miencraft

import com.wittano.mineserv.components.server.ServerManager
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.interfaces.service.ServerService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class ServerManagerService(
    private val manager: ServerManager
) : ServerService {
    override fun start(data: Server): Mono<Server> =
        data.toMono().doOnNext {
            manager.start(it)
        }

    override fun stop(data: Server): Mono<Server> =
        data.toMono().doOnNext {
            manager.stop(it)
        }

    override fun create(data: Server): Mono<Server> =
        data.toMono().doOnNext {
            manager.create(it)
        }

    override fun delete(entity: Server): Mono<Server> =
        entity.toMono().doOnNext {
            manager.delete(it)
        }
}