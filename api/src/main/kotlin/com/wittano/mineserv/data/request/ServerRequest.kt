package com.wittano.mineserv.data.request

data class ServerRequest(
    val name: String,
    val owner_id: Long,
    val version_id: Long,
) {
}