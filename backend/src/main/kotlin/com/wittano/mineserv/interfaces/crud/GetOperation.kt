package com.wittano.mineserv.interfaces.crud

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GetOperation<T> {
    fun findOne(id: Long): Mono<T>
    fun findAll(): Flux<T>
}