package com.wittano.mineserv.interfaces

import com.wittano.mineserv.components.exceptions.MappedException
import kotlin.jvm.Throws

interface Mapper<T, K> {
    @Throws(MappedException::class)
    fun map(data: T): K
}