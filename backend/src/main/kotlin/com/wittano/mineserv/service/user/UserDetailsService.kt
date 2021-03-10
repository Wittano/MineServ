package com.wittano.mineserv.service.user

import com.wittano.mineserv.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * Service. which for search user by username
 */
@Service("userAuth")
class UserDetailsService(
    private val repo: UserRepository,
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> =
        try {
            Mono.just(repo.findByUsername(username!!)!!).map {
                User(
                    it!!.username,
                    it.password,
                    mutableListOf(SimpleGrantedAuthority("USER"))
                )
            }
        } catch (e: NullPointerException) {
            Mono.empty()
        }
}