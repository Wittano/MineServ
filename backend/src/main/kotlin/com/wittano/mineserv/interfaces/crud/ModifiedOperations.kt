package com.wittano.mineserv.interfaces.crud

import reactor.core.publisher.Mono

interface ModifiedOperations<T> {
    fun create(data: T): Mono<T>
    fun delete(entity: T): Mono<T>
}