package com.wittano.mineserv.data.request

data class ServerRequest(
    val name: String,
    var owner_id: Long?,
    val version_id: Long,
)