package com.wittano.mineserv.interfaces.crud

import reactor.core.publisher.Mono

interface UpdateModification<T> {
    fun update(merge: T): Mono<T>
}