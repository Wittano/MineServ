package com.wittano.mineserv.controller

import com.wittano.mineserv.components.utils.JwtUtil
import com.wittano.mineserv.data.User
import com.wittano.mineserv.data.UserRequest
import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.repository.UserRepository
import com.wittano.mineserv.utils.DataReader
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

    @Autowired
    lateinit var userRepo: UserRepository

    private val testUser: UserRequest =
        UserRequest("wittano", "1234567890")

    @Test
    fun register_ShouldReturnUserDataWithoutPassword() {
        val response = client
            .post()
            .uri("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testUser))
            .exchange()
            .expectStatus().isCreated
            .expectBody(Response::class.java)
            .returnResult()

        try {
            assertNull(response.responseBody?.message)
            assertNotNull(response.responseBody?.data)

            val data = DataReader.getData<Map<String, String>>(response)

            assertNull(data["password"])
            assertEquals(data["username"], testUser.username)
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
        client
            .post()
            .uri("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(UserRequest(name, testUser.password)))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Response::class.java)
    }

    @Test
    fun auth_ShouldReturnJwtToken() {
        try {
            userRepo.save(
                User(
                    null,
                    testUser.username,
                    PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(testUser.password)
                )
            )
        } catch (e: Exception) {
        }

        val res = client
            .post()
            .uri("/api/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testUser))
            .exchange()
            .expectStatus().isOk
            .expectBody(Response::class.java)
            .returnResult()

        assertTrue(jwtUtil.isValid(DataReader.getData<Map<String, String>>(res)["token"]!!))
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "", "canksdjhfgt8wer4", "1 or 1 --", "!~%^", "1234", "1234 5", "1 2 3 4 5"
        ]
    )
    fun auth_ShouldReturnErrorMessage(password: String) {
        try {
            userRepo.save(
                User(
                    null,
                    testUser.username,
                    PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(testUser.password)
                )
            )
        } catch (e: Exception) {
        }

        val invalidUser: UserRequest = testUser
        invalidUser.password = password

        client
            .post()
            .uri("/api/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(invalidUser))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(Response::class.java)
    }
}