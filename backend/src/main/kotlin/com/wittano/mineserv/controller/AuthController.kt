package com.wittano.mineserv.controller

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.User
import com.wittano.mineserv.data.UserRequest
import com.wittano.mineserv.data.response.JwtResponse
import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.data.views.DefaultView
import com.wittano.mineserv.service.user.UserAuthService
import com.wittano.mineserv.service.user.UserService
import org.springframework.batch.item.validator.Validator
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/api")
class AuthController(
    private val authService: UserAuthService,
    private val userService: UserService,
    private val validator: Validator<UserRequest>
) {
    /**
     * Authorization user
     * @param userRequest username and password, which is required to verify user
     * @return JWT token
     */
    @PostMapping("/auth")
    fun auth(@RequestBody userRequest: UserRequest): Mono<Response<JwtResponse>> =
        validator.validate(userRequest).toMono().flatMap {
            authService.auth(userRequest).map {
                Response(JwtResponse(it))
            }
        }

    /**
     * Create user account
     * @param userRequest username and password, which is required to create user
     * @return User data, which was created
     */
    @PostMapping("/user")
    @JsonView(DefaultView.Companion.External::class)
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody userRequest: UserRequest): Mono<Response<User>> =
        validator.validate(userRequest).toMono().flatMap {
            userService.save(userRequest).map {
                Response(it)
            }
        }
}