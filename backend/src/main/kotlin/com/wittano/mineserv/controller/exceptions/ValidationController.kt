package com.wittano.mineserv.controller.exceptions

import com.wittano.mineserv.components.exceptions.IllegalOperationException
import com.wittano.mineserv.components.exceptions.ServerNotFoundException
import com.wittano.mineserv.components.utils.reponse.ResponseCreator
import org.springframework.batch.item.validator.ValidationException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationController {
    @ExceptionHandler(ValidationException::class)
    fun validationException(exception: ValidationException) =
        ResponseCreator.getErrorResponse(exception)

    @ExceptionHandler(ServerNotFoundException::class)
    fun serverNotFoundException(exception: ServerNotFoundException) =
        ResponseCreator.getErrorResponse(exception)

    @ExceptionHandler(IllegalOperationException::class)
    fun illegalOperationException(exception: ServerNotFoundException) =
        ResponseCreator.getErrorResponse(exception)

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(exception: BadCredentialsException) =
        ResponseCreator.getErrorResponse(exception)
}