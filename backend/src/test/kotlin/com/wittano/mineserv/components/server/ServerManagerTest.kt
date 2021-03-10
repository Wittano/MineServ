package com.wittano.mineserv.components.server

import com.wittano.mineserv.config.scheduling.InsertLinks
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import com.wittano.mineserv.repository.ServerRepository
import com.wittano.mineserv.repository.UserRepository
import com.wittano.mineserv.repository.VersionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@SpringBootTest
internal class ServerManagerTest {

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var serverRepo: ServerRepository

    @Autowired
    private lateinit var versionRepo: VersionRepository

    @Autowired
    private lateinit var manager: ServerManager

    @Autowired
    private lateinit var insertLinks: InsertLinks

    @Value("\${project.download.dir}")
    private lateinit var downloadDir: String

    @BeforeEach
    private fun addUser() {
        try {
            insertLinks.insert()
        } catch (e: Exception) {
        }
        try {
            userRepo.save(User(null, "test", "test12345"))
            userRepo.save(User(null, "test2", "test12345"))
        } catch (e: Exception) {
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "1.16.5", "1.5.2"
        ]
    )
    fun addServer_ShouldAddServerToDatabase(version: String) {
        val server = Server(
            null,
            "testServer${version}",
            userRepo.findByUsername("test")!!,
            versionRepo.findFirstByVersion(version)!!,
            null
        )


        manager.create(server)

        val testServer = serverRepo.findAllByOwner(server.owner).filter {
            it.name == server.name
        }[0]

        assertNotNull(testServer)
        assertEquals(testServer.name, server.name)

        val path = Path.of("${downloadDir}/${testServer.name}")

        assertTrue(Files.exists(path))
        assertEquals(3, path.toFile().list().filter {
            it == "server.properties"
                    || it == "eula.txt"
                    || it == "minecraft_server.${testServer.version.version}.jar"
        }.size)

        val properties = Properties()

        properties.load(
            FileInputStream(
                Path.of("${downloadDir}/${testServer.name}/eula.txt").toFile()
            )
        )

        assertEquals("true", properties["eula"])

        properties.load(
            FileInputStream(
                Path.of("${downloadDir}/${testServer.name}/server.properties").toFile()
            )
        )

        assertEquals("127.0.0.1", properties["server-ip"])
    }

    @Test
    fun runServer_ShouldRunServer() {
        val server = Server(
            null,
            "testServer",
            userRepo.findByUsername("test")!!,
            versionRepo.findFirstByVersion("1.16.5")!!,
            null
        )

        manager.create(server)
        manager.start(server)

        val testServer = serverRepo.findAllByOwner(server.owner).filter {
            it.name == server.name
        }[0]

        assertNotNull(testServer.pid)

        manager.stop(testServer)
    }

    @Test
    fun deleteServer_ShouldDeleteServer() {
        val server = Server(
            null,
            "testServer3",
            userRepo.findByUsername("test")!!,
            versionRepo.findFirstByVersion("1.16.5")!!,
            null
        )

        manager.create(server)
        manager.delete(server)

        assertEquals(0, serverRepo.findAllByOwner(server.owner).filter {
            it.name == server.name
        }.size)
    }
}