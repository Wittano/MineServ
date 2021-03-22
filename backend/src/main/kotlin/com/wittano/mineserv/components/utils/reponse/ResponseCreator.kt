package com.wittano.mineserv.components.utils.reponse

import com.wittano.mineserv.data.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseCreator {
    companion object {
        fun <T> createResponse(
            message: String,
            status: HttpStatus = HttpStatus.BAD_REQUEST
        ): ResponseEntity<Response<T>> {
            return ResponseEntity.status(status).body(Response(message = message))
        }

        fun getErrorResponse(exception: Exception): ResponseEntity<Response<Nothing>> =
            createResponse(exception.message!!)
    }
}