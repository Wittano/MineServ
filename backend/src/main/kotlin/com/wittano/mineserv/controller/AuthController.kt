package com.wittano.mineserv.controller

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.UserRequest
import com.wittano.mineserv.data.views.DefaultView
import com.wittano.mineserv.service.user.UserAuthService
import com.wittano.mineserv.service.user.UserService
import org.springframework.batch.item.validator.ValidationException
import org.springframework.batch.item.validator.Validator
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.sql.SQLException

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
     * @return JWT token or error message, when somethings went wrong
     */
    @PostMapping("/auth")
    fun auth(@RequestBody userRequest: UserRequest): Mono<ResponseEntity<String>> {
        return try {
            validator.validate(userRequest)

            return authService.auth(userRequest).map {
                ResponseEntity.ok(it)
            }.onErrorResume {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(it.message).toMono()
            }
        } catch (e: ValidationException) {
            ResponseEntity.badRequest().body(e.message).toMono()
        }
    }

    /**
     * Create user account
     * @param userRequest username and password, which is required to create user
     * @return User data or error message
     */
    @PostMapping("/user")
    @JsonView(DefaultView.Companion.External::class)
    fun register(@RequestBody userRequest: UserRequest): Mono<ResponseEntity<Any>> {
        return try {
            validator.validate(userRequest)

            return userService.save(userRequest).map {
                ResponseEntity.status(HttpStatus.CREATED).body(it as Any)
            }.onErrorResume {
                ResponseEntity.badRequest().toMono().map {
                    if (it is SQLException) {
                        it.body("User exists!")
                    } else {
                        it.body("Invalid login or password")
                    }
                }
            }
        } catch (e: ValidationException) {
            Mono.just(ResponseEntity.badRequest().body(e.message))
        }
    }
}