package com.wittano.mineserv.controller

import com.wittano.mineserv.config.scheduling.InsertLinks
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import com.wittano.mineserv.data.response.Response
import com.wittano.mineserv.repository.ServerRepository
import com.wittano.mineserv.repository.UserRepository
import com.wittano.mineserv.repository.VersionRepository
import com.wittano.mineserv.utils.DataReader
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
        links.insert()

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
            DataReader.getData(
                client
                    .post()
                    .uri("/api/server")
                    .body(BodyInserters.fromValue(testServer))
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Response::class.java)
                    .returnResult()
            )
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
    }

    @Test
    fun startStart() {
        val testServer = createTestServer().takeIf {
            it.id != null
        }

        val id: Long? = if (testServer != null) {
            testServer.id
        } else {
            serverRepo.findAllByOwner(user)[0].id
        }

        client
            .put()
            .uri("/api/server/start/${id}")
            .exchange()
            .expectStatus().isOk
    }
}