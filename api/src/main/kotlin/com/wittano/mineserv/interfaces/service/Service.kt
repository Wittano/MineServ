package com.wittano.mineserv.interfaces.service

import reactor.core.publisher.Mono

/**
 * General service, which can run and stop some service, program, task etc.
 */
interface Service<T> {
    /**
     * Run some implemented task
     * @param data Data, which will be used for run task
     * @return Reactive Mono object, with same or modified data, which will be given as parameter
     * @see Mono
     */
    fun start(data: T): Mono<T>

    /**
     * Stop some implemented task
     * @param data Data, which will be used for stop task
     * @return Reactive Mono object, with same or modified data, which will be given as parameter
     * @see Mono
     */
    fun stop(data: T): Mono<T>
}