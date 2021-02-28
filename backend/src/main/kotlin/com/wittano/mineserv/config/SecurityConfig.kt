package com.wittano.mineserv.config

import com.wittano.mineserv.config.secuirty.JwtServerContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtServerContext: JwtServerContextRepository,
    private val authManager: ReactiveAuthenticationManager
) {

    @Bean
    fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .cors().and()
        .csrf().disable()
        .authorizeExchange()
        .anyExchange().permitAll()
        .and()
        .authenticationManager(authManager).securityContextRepository(jwtServerContext)
        .formLogin().disable()
        .build()
}