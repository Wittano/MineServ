package com.wittano.mineserv.interfaces.crud

import reactor.core.publisher.Mono

interface UpdateOperation<T> {
    fun update(merge: T): Mono<T>
}