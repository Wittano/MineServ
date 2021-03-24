package com.wittano.mineserv.config

import com.wittano.mineserv.config.secuirty.JwtServerContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtServerContext: JwtServerContextRepository,
    private val authManager: ReactiveAuthenticationManager
) {
    @Bean
    fun encoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    @Primary
    @Profile("test")
    fun testConfiguration(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .cors().disable()
        .csrf().disable()
        .authorizeExchange()
        .anyExchange().permitAll().and()
        .authenticationManager(authManager).securityContextRepository(jwtServerContext)
        .formLogin().disable()
        .build()

    @Bean
    fun defaultConfiguration(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .httpBasic().disable()
        .cors().and()
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers(HttpMethod.POST, "/api/auth", "/api/user").permitAll()
        .pathMatchers(HttpMethod.OPTIONS, "/api/auth", "/api/user").permitAll()
        .anyExchange().authenticated().and()
        .authenticationManager(authManager).securityContextRepository(jwtServerContext)
        .formLogin().disable()
        .logout().disable()
        .build()
}