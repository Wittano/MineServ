package com.wittano.mineserv.components.utils.reponse

import com.wittano.mineserv.data.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Tool to create simple ResponseEntity
 * @see ResponseEntity
 */
class ResponseCreator {
    companion object {
        /**
         * Create ResponseEntity object
         * @param message Message which, will be passed on
         * @param data Special data, which will be passed on. Default value is null
         * @param status Http status. Default value is Http status 400
         * @return Http response object
         */
        fun <T> createResponse(
            message: String,
            data: T? = null,
            status: HttpStatus = HttpStatus.BAD_REQUEST
        ): ResponseEntity<Response<T>> {
            return ResponseEntity.status(status).body(Response(data, message))
        }

        /**
         * Create simple error HTTP response
         * @param exception Exception from which will be pulled message error, which will be put in HTTP response
         * @return HTTP response with status 400, with error message
         */
        fun getErrorResponse(exception: Exception): ResponseEntity<Response<Nothing>> =
            createResponse(exception.message!!)
    }
}