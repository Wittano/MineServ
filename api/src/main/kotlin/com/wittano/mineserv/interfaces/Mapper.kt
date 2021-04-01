package com.wittano.mineserv.interfaces

import com.wittano.mineserv.components.exceptions.MappedException

/**
 * General ObjectMapper
 * @param T type of object, which will be mapped
 * @param K type of object, which will be returned
 */
interface Mapper<T, K> {
    /**
     * Map T object to K object
     * @param data source object, from which will be got data
     * @throws MappedException throws, when appear problem during mapping
     * @see MappedException
     */
    @Throws(MappedException::class)
    fun map(data: T): K
}