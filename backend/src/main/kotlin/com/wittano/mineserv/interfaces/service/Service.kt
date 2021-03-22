package com.wittano.mineserv.interfaces.service

import reactor.core.publisher.Mono

interface Service<T> {
    fun start(data: T): Mono<T>
    fun stop(data: T): Mono<T>
}