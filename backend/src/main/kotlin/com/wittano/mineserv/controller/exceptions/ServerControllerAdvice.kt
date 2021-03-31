package com.wittano.mineserv.controller.exceptions

import com.wittano.mineserv.components.utils.reponse.ResponseCreator
import com.wittano.mineserv.controller.ServerController
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [ServerController::class])
class ServerControllerAdvice {
    @ExceptionHandler(Exception::class)
    fun exception(e: Exception) = ResponseCreator.getErrorResponse(e)
}