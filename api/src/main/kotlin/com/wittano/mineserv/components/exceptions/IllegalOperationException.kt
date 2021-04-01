package com.wittano.mineserv.components.exceptions

/**
 * Thrown to indicate, that action, which someone was tried to do is unauthorised
 */
class IllegalOperationException(msg: String) : Exception(msg)