package com.wittano.mineserv.components.server

import com.wittano.mineserv.components.exceptions.VersionException
import com.wittano.mineserv.repository.VersionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Path

/**
 * Component, which downloads minecraft server
 */
@Component
class ServerDownloader(
    private val repo: VersionRepository
) {

    @Value("\${project.download.dir}")
    private lateinit var path: String

    private fun createFile(path: String) = File(path).also {
        if (!it.exists()) {
            try {
                it.createNewFile()
            } catch (e: IOException) {
                Files.createDirectory(Path.of(it.absolutePath.substringBeforeLast('/')))
                it.createNewFile()
            }
        }
    }

    /**
     * Download minecraft_server.jar to special directory
     * @param version version of minecraft server
     * @param name directory name, which will be downloaded server.jar
     * @throws VersionException throws, when version doesn't exist in database
     */
    @Throws(VersionException::class)
    fun download(name: String, version: String) {
        if (!repo.existsByVersion(version)) {
            throw VersionException("Version $version doesn't exist!")
        }

        val file = name.let {
            // Create empty file, which will be downloaded minecraft_server.jar
            if (it.isNotEmpty()) {
                createFile("${path}/${it}/minecraft_server.${version}.jar")
            } else {
                createFile("${path}/minecraft_server.${version}.jar")
            }
        }

        if (Files.size(file.toPath()) != 0L) {
            return
        }

        //Download minecraft_server.jar
        FileOutputStream(file).channel.transferFrom(
            Channels.newChannel(
                URL(repo.findFirstByVersion(version)?.link).openStream()
            ),
            0,
            Long.MAX_VALUE
        )
    }
}