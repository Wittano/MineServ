package com.wittano.mineserv.interfaces.service

import reactor.core.publisher.Mono

interface Service<T> {
    fun start(data: T): Mono<Void>
    fun stop(data: T): Mono<Void>
}