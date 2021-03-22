package com.wittano.mineserv.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.wittano.mineserv.data.response.Response
import org.springframework.test.web.reactive.server.EntityExchangeResult

class DataReader {
    companion object {
        inline fun <reified T> getData(response: EntityExchangeResult<Response<*>>): T = try {
            response.responseBody?.data as T
        } catch (_: ClassCastException) {
            ObjectMapper().convertValue(response.responseBody!!.data, T::class.java)
        }
    }
}