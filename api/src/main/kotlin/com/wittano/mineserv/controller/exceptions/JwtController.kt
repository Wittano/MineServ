package com.wittano.mineserv.controller.exceptions

import com.wittano.mineserv.components.utils.reponse.ResponseCreator
import io.jsonwebtoken.JwtException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class JwtController {
    @ExceptionHandler(JwtException::class)
    fun signatureException(e: JwtException) =
        ResponseCreator.createResponse<Nothing>(e.message!!)
}