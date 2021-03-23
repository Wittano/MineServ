package com.wittano.mineserv.data.response

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView

/**
 * General Http response model object
 */
data class Response<T>(
    @JsonView(DefaultView.Companion.External::class)
    val data: T? = null,
    @JsonView(DefaultView.Companion.External::class)
    val message: String? = null,
)