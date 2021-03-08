package com.wittano.mineserv.service.user

import com.wittano.mineserv.components.utils.JwtUtil
import com.wittano.mineserv.data.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Service for authorization user
 */
@Service
class UserAuthService(
    private val authManager: ReactiveAuthenticationManager,
    private val jwtUtil: JwtUtil
) {
    private val logger = LoggerFactory.getLogger(UserAuthService::class.simpleName)


    /**
     * Authorize user
     * @return JWT token
     */
    fun auth(userRequest: UserRequest): Mono<String> =
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userRequest.username,
                userRequest.password,
                mutableListOf(SimpleGrantedAuthority("USER"))
            )
        ).doOnError {
            logger.error("${it.cause} ${it.message}")
        }.doOnSuccess {
            SecurityContextImpl(it)
        }.then(Mono.just(jwtUtil.create(userRequest.username)))
}