package com.wittano.mineserv.config.beans

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthorizationManager(
    @Qualifier("userAuth") private val userService: ReactiveUserDetailsService
) {
    @Bean
    fun authManager(): ReactiveAuthenticationManager =
        UserDetailsRepositoryReactiveAuthenticationManager(userService)
}