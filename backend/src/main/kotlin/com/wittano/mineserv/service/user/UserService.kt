package com.wittano.mineserv.service.user

import com.wittano.mineserv.data.User
import com.wittano.mineserv.data.UserRequest
import com.wittano.mineserv.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * Service for management user
 */
@Service
class UserService(
    private val repo: UserRepository,
    private val encoder: PasswordEncoder
) {
    fun save(userRequest: UserRequest): Mono<User> {
        return userRequest.toMono().map {
            repo.save(User(null, userRequest.username, encoder.encode(userRequest.password)))
        }
    }
}