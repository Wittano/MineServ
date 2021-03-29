package com.wittano.mineserv.config.beans

import com.wittano.mineserv.components.utils.JwtUtil
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class AuthorizationManager(
    private val jwtUtil: JwtUtil
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> =
        authentication.toMono().flatMap {
            if (jwtUtil.isValid(it.credentials as String)) {
                UsernamePasswordAuthenticationToken(
                    it.principal,
                    it.credentials,
                    mutableListOf(SimpleGrantedAuthority("USER"))
                ).toMono()
            } else {
                Mono.error(BadCredentialsException("Invalid login or password"))
            }
        }
}