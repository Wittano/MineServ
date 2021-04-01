package com.wittano.mineserv.config.converter

import com.wittano.mineserv.components.utils.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class AuthenticationConverter(
    private val jwtUtil: JwtUtil
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> =
        exchange!!.request.headers["Authorization"].toString()
            .toMono().filter {
                !it.isNullOrEmpty() && it != "null"
            }.map {
                it.replace(Regex("(Bearer)|(\\])|(\\[)"), "").trim()
            }
            .map {
                UsernamePasswordAuthenticationToken(
                    jwtUtil.decode(it).subject,
                    it
                )
            }
}