package com.wittano.mineserv.components.exceptions

/**
 * Exception, which will be thrown, when server, which was given by user, won't be found
 */
class ServerNotFoundException(val msg: String) : Exception(msg)