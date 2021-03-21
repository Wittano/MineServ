package com.wittano.mineserv.controller

import com.wittano.mineserv.config.scheduling.InsertLinks
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import com.wittano.mineserv.repository.ServerRepository
import com.wittano.mineserv.repository.UserRepository
import com.wittano.mineserv.repository.VersionRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "60000000000")
internal class ServerControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    @Autowired
    private lateinit var links: InsertLinks

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var serverRepo: ServerRepository

    @Autowired
    private lateinit var versionRepo: VersionRepository

    private val user = User(null, "testname", "1234567890asdf")

    fun createTestServer(name: String = "testServer"): Server {
        try {
            links.insert()
        } catch (e: Exception) {
        }

        val testUser = try {
            userRepo.findAll().toList()[0]
        } catch (e: IndexOutOfBoundsException) {
            userRepo.save(user)
        }

        val testServer = Server(
            null,
            name,
            testUser,
            versionRepo.findFirstByVersion("1.16.5")!!,
            null
        )

        return try {
            serverRepo.findAll().toList()[0]
        } catch (e: IndexOutOfBoundsException) {
            client
                .post()
                .uri("/api/server")
                .body(BodyInserters.fromValue(testServer))
                .exchange()
                .expectStatus().isCreated
                .expectBody(Server::class.java)
                .returnResult().responseBody!!
        }
    }

    @Test
    fun createServer() {
        createTestServer("server")
    }

    @Test
    fun deleteServer() {
        val testServer = createTestServer()

        client
            .delete()
            .uri("/api/server/${testServer.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody().isEmpty
    }

    @Test
    fun stopServer() {
        val testServer = createTestServer()

        client
            .put()
            .uri("/api/server/start/${testServer.id}")
            .exchange()

        client
            .put()
            .uri("/api/server/stop/${testServer.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody().isEmpty
    }

    @Test
    fun startStart() {
        val testServer = createTestServer()

        client
            .put()
            .uri("/api/server/start/${testServer.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody().isEmpty
    }
}