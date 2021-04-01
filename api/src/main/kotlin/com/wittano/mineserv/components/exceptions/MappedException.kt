package com.wittano.mineserv.components.exceptions

/**
 * Runtime exception, which informs about problem during mapping
 */
class MappedException(override val message: String?) : RuntimeException(message)