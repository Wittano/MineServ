package com.wittano.mineserv.service.user

import com.wittano.mineserv.components.utils.JwtUtil
import com.wittano.mineserv.data.request.UserRequest
import com.wittano.mineserv.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * Service for authorization user
 */
@Service
class UserAuthService(
    private val jwtUtil: JwtUtil,
    private val repo: UserRepository,
    private val encoder: PasswordEncoder
) {

    /**
     * Authorize user
     * @return JWT token
     */
    fun auth(userRequest: UserRequest): Mono<String> =
        userRequest.toMono().doOnNext {
            if (!encoder.matches(it.password, repo.findByUsername(it.username)?.password)) {
                throw BadCredentialsException("Invalid login or password")
            }
        }.flatMap {
            jwtUtil.create(it.username).toMono()
        }
}