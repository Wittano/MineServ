package com.wittano.mineserv.controller

import com.wittano.mineserv.components.utils.JwtUtil
import com.wittano.mineserv.data.User
import com.wittano.mineserv.data.UserRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest
@AutoConfigureWebTestClient
internal class AuthControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @SpyBean
    lateinit var jwtUtil: JwtUtil

    private val testUser: UserRequest =
        UserRequest("wittano", "1234567890")

    private data class UserResponse(
        val id: Int?,
        val username: String
    )

    @Test
    fun register_ShouldReturnUserDataWithoutPassword() {
        val response = client
            .post()
            .uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testUser))
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserResponse::class.java)
            .returnResult()

        assertNotNull(response)

        try {
            assertEquals(response.responseBody!!.username, testUser.username)
        } catch (e: NullPointerException) {
            fail()
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "", "1 or 1 --", "$%^%^$&", "wittano!", "    wittano"
        ]
    )
    fun register_ShouldReturnBadRequestStatus(name: String) {
        val res = client
            .post()
            .uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserRequest(name, testUser.password)))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()

        assertEquals("Invalid login or password", res.responseBody)
    }

    @Test
    fun auth_ShouldReturnJwtToken() {
        client
            .post()
            .uri("/user")
            .bodyValue(
                User(
                    null,
                    testUser.username,
                    PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(testUser.password)
                )
            )
            .exchange()

        val response = client
            .post()
            .uri("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testUser))
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()

        assertTrue(jwtUtil.isValid(response.responseBody!!))
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "", "canksdjhfgt8wer4", "1 or 1 --", "!~%^", "1234", "1234 5", "1 2 3 4 5"
        ]
    )
    fun auth_ShouldReturnErrorMessage(password: String) {
        client
            .post()
            .uri("/user")
            .bodyValue(
                User(
                    null,
                    testUser.username,
                    PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(testUser.password)
                )
            )
            .exchange()

        val res = client
            .post()
            .uri("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testUser))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()

        assertEquals("Invalid Credentials", res.responseBody)
    }
}