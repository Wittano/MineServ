package com.wittano.mineserv.components.server

import com.wittano.mineserv.components.exceptions.IllegalOperationException
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.repository.ServerRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class ServerManager(
    private val repo: ServerRepository,
    private val downloader: ServerDownloader,
    private val properties: ServerProperties
) {

    @Value("\${project.download.dir}")
    private lateinit var downloadDir: String

    private val logger = LoggerFactory.getLogger(ServerManager::class.qualifiedName)

    private fun getJRE(): String {
        // Check if OS is Windows
        return if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            System.getProperty("java.home") + "/bin/java.exe"
        } else {
            System.getProperty("java.home") + "/bin/java"
        }
    }

    fun init(server: Server) {
        downloader.download(server.name, server.version.version)

        val process = ProcessBuilder(
            getJRE(),
            "-jar",
            "minecraft_server.${server.version.version}.jar",
            "--nogui",
            "--init"
        ).directory(File("${downloadDir}/${server.name}")).start()

        if (server.version > "1.7.9") {
            process.waitFor()
        } else {
            Executors.newSingleThreadExecutor().execute {
                process.waitFor(3, TimeUnit.SECONDS)
                ProcessHandle.of(process.pid()).ifPresent {
                    it.destroy()
                }
            }
        }

        properties.default(server.name)
        repo.save(server)
    }

    @Throws(IllegalOperationException::class)
    fun run(server: Server) {
        if (repo.findAllByOwner(server.owner)!!.none {
                it.name == server.name
            }) {
            throw IllegalOperationException("You can't run server, if you aren't owner")
        }

        server.pid = ProcessBuilder("java")
            .command(
                getJRE(),
                "-jar",
                "minecraft_server.${server.version.version}.jar",
                "--nogui"
            )
            .directory(Path.of("${downloadDir}/${server.name}").toFile())
            .start().pid()

        repo.save(server)
    }

    fun stop(server: Server) {
        ProcessHandle.of(server.pid!!).ifPresent {
            it.destroy()
            it.onExit().completeAsync({
                logger.info("Server ${server.name} was closed by ${server.owner.name}")

                server.pid = null
                repo.save(server)

                it
            }, Executors.newSingleThreadExecutor())
        }
    }

    fun delete(server: Server) {
        FileSystemUtils.deleteRecursively(Path.of("${downloadDir}/${server.name}"))
        repo.delete(server)
    }
}