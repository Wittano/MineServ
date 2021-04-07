package com.wittano.mineserv.components.server

import com.wittano.mineserv.enums.server.ServerFiles
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*

/**
 * Component for modification .properties files
 */
@Component
class ServerProperties {

    @Value("\${project.download.dir}")
    private lateinit var downloadDir: String

    /**
     * Get path to minecraft server directory
     * @param dir Directory, which contain minecraft server
     * @param name File name, which is located in dir
     */
    private fun getPath(dir: String, name: String): Path =
        Path.of("${downloadDir}/${dir}/${name}")

    /**
     * Load file as Property object
     * @param dir Directory name. Usually is server name
     * @param name Name file, which will be changed. Default is server.properties
     */
    fun load(dir: String, name: String = ServerFiles.SETTINGS.file) = Properties().also {
        try {
            it.load(FileInputStream(getPath(dir, name).toFile()))
        } catch (e: FileNotFoundException) {
        }
    }

    private fun defaultSettings(name: String) = load(name).let {
        it["server-ip"] = "127.0.0.1"
        save(name, ServerFiles.SETTINGS.file, it)
    }

    private fun enableEULA(name: String) = load(name, ServerFiles.EULA.file).let {
        it["eula"] = "true"
        save(name, ServerFiles.EULA.file, it)
    }

    /**
     * Update properties file
     * @param dir Directory name, which has the same name as server
     * @param name Properties file, which will be updated. Default is server.properties
     * @param properties Properties, which will be inserted to .properties file
     */
    fun save(dir: String, name: String = ServerFiles.SETTINGS.file, properties: Properties) =
        properties.store(FileOutputStream(getPath(dir, name).toFile()), "")

    /**
     * Load default settings configuration for server
     * @param name Server name
     */
    fun default(name: String) {
        enableEULA(name)
        defaultSettings(name)
    }
}