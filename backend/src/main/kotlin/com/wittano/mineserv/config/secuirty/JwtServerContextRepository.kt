package com.wittano.mineserv.config.secuirty

import com.wittano.mineserv.components.utils.JwtUtil
import com.wittano.mineserv.repository.UserRepository
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtServerContextRepository(
    private val jwtUtil: JwtUtil,
    private val repo: UserRepository,
    private val auth: ReactiveAuthenticationManager
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> =
        Mono.just(exchange!!.request.headers["Authorization"].toString()).map {
            if (it == "null" || it.length < 7) {
                ""
            } else {
                it.substring(7)
            }
        }.defaultIfEmpty("").flatMap {
            if (it != null && it.isNotBlank()) {
                Mono.just(jwtUtil.decode(it).subject).flatMap { username ->
                    Mono.just(repo.findByUsername(username)!!).flatMap { user ->
                        auth.authenticate(
                            UsernamePasswordAuthenticationToken(
                                user.username,
                                user.password
                            )
                        ).map { auth ->
                            SecurityContextImpl(auth)
                        }
                    }
                }
            } else {
                Mono.empty()
            }
        }
}