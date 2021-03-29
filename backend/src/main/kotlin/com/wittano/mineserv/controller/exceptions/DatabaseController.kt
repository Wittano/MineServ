package com.wittano.mineserv.controller.exceptions

import com.wittano.mineserv.components.utils.reponse.ResponseCreator.Companion.createResponse
import com.wittano.mineserv.data.response.Response
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLDataException
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.SQLTimeoutException

@RestControllerAdvice
class DatabaseController {
    @ExceptionHandler(SQLException::class)
    fun sqlExceptions(exception: SQLException): ResponseEntity<Response<Nothing>> = when (exception) {
        is SQLIntegrityConstraintViolationException -> {
            createResponse("Value must be unique and not null")
        }
        is SQLDataException -> {
            createResponse("Invalid data")
        }
        is SQLTimeoutException -> {
            createResponse("Timeout!")
        }
        else -> {
            createResponse(exception.message!!)
        }
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun uniqueExceptions(exception: DataIntegrityViolationException) =
        createResponse<Nothing>("Data is exist")
}