package com.wittano.mineserv.interfaces.crud

import reactor.core.publisher.Mono

/**
 * General interface, which represent modified operation
 */
interface ModifiedOperations<T> {
    /**
     * Create some object
     * @param data Data, which will be used for create some object
     * @return Reactive Mono object, which contain created object
     * @see Mono
     */
    fun create(data: T): Mono<T>

    /**
     * Delete some object
     * @param entity Entity, which will be used for delete some object
     * @return Reactive Mono object, which contain deleted object
     * @see Mono
     */
    fun delete(entity: T): Mono<T>
}