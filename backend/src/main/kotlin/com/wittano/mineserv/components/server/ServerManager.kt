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

/**
 * Component for manage minecraft server
 */
@Component
class ServerManager(
    private val repo: ServerRepository,
    private val downloader: ServerDownloader,
    private val properties: ServerProperties
) {

    @Value("\${project.download.dir}")
    private lateinit var downloadDir: String
    private val logger = LoggerFactory.getLogger(ServerManager::class.qualifiedName)

    /**
     * Get path to JRE binary
     */
    private fun getJRE(): String =
        // Check if OS is Windows
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            System.getProperty("java.home") + "/bin/java.exe"
        } else {
            System.getProperty("java.home") + "/bin/java"
        }

    /**
     * Create minecraft server
     * @param server data about server, which will be created
     * @return Server data, which was saved in database
     */
    fun create(server: Server): Server {
        downloader.download(server.name, server.version.version)

        val process = ProcessBuilder(
            getJRE(),
            "-jar",
            "minecraft_server.${server.version.version}.jar",
            "--nogui",
            "--init"
        ).directory(File("${downloadDir}/${server.name}")).start()

        // In minecraft 1.8 and higher, Mojang change server.jar and add some flag to server.
        // --init - create necessary files to create configuration for any server
        // In earlier version, didn't have any flags and necessary files was created right after run server.
        // So I can stop sever on another thread and don't have to wait, until he finished him job
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

        return repo.save(server)
    }

    /**
     *  Run minecraft server
     *  @param server Server, which will be run
     *  @throws IllegalOperationException throws, if someone will tried run server, when he isn't owner.
     *  Only owner can run server
     */
    @Throws(IllegalOperationException::class)
    fun start(server: Server) {
        if (repo.findAllByOwner(server.owner).none {
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

    /**
     * Stop server without saving words
     * @param server Server, which will be stopped
     */
    fun stop(server: Server) {
        ProcessHandle.of(server.pid!!).ifPresent {
            it.destroy()
            it.onExit().completeAsync({
                logger.info("Server ${server.name} was closed by ${server.owner.username}")

                server.pid = null
                repo.save(server)

                it
            }, Executors.newSingleThreadExecutor())
        }
    }

    /**
     * Remove server from database and storage
     * @param server Server, which will be deleted
     */
    fun delete(server: Server) {
        FileSystemUtils.deleteRecursively(Path.of("${downloadDir}/${server.name}"))
        repo.delete(server)
    }
}