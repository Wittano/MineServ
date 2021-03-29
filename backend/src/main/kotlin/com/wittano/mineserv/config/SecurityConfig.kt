package com.wittano.mineserv.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig(
    private val authManager: ReactiveAuthenticationManager,
    private val converter: ServerAuthenticationConverter
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
        .formLogin().disable()
        .build()

    @Bean
    fun defaultConfiguration(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationFilter = AuthenticationWebFilter(authManager)
        authenticationFilter.setServerAuthenticationConverter(converter)

        return http
            .httpBasic().disable()
            .cors().and()
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.POST, "/api/auth", "/api/user").permitAll()
            .pathMatchers(HttpMethod.OPTIONS, "/api/auth", "/api/user").permitAll()
            .anyExchange().authenticated().and()
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .formLogin().disable()
            .logout().disable()
            .build()
    }
}