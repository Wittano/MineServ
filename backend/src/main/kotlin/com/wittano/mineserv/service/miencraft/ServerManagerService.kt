package com.wittano.mineserv.service.miencraft

import com.wittano.mineserv.components.server.ServerManager
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.interfaces.service.ServerService
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ServerManagerService(
    private val manager: ServerManager,
    private val repo: ServerRepository
) : ServerService {
    override fun start(data: Server): Mono<Void> {
        return Mono.just(data).doOnNext {
            manager.start(it)
        }.flatMap {
            Mono.empty()
        }
    }

    override fun stop(data: Server): Mono<Void> {
        return Mono.just(data).doOnNext {
            manager.stop(it)
        }.flatMap {
            Mono.empty()
        }
    }

    override fun create(data: Server): Mono<Server> =
        Mono.just(data).doOnNext {
            manager.create(it)
        }.doOnSuccess {
            repo.save(it)
        }

    override fun delete(entity: Server): Mono<Void> {
        return Mono.just(entity).doOnNext {
            manager.delete(it)
        }.flatMap {
            Mono.empty()
        }
    }
}