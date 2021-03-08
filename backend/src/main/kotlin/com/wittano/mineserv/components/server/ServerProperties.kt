package com.wittano.mineserv.components.server

import com.wittano.mineserv.enums.ServerFiles
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.*

@Component
class ServerProperties {

    @Value("\${project.download.dir}")
    private lateinit var downloadDir: String


    private fun getPath(dir: String, name: String): Path =
        Path.of("${downloadDir}/${dir}/${name}")

    /**
     * Load file as Property object
     */
    private fun load(dir: String, name: String) = Properties().also {
        try {
            it.load(FileInputStream(getPath(dir, name).toFile()))
        } catch (e: FileNotFoundException) {
        }
    }

    private fun defaultSettings(name: String) = load(name, ServerFiles.SETTINGS.file).let {
        it["server-ip"] = "127.0.0.1"
        save(name, ServerFiles.SETTINGS.file, it)
    }

    private fun enableEULA(name: String) = load(name, ServerFiles.EULA.file).let {
        it["eula"] = "true"
        save(name, ServerFiles.EULA.file, it)
    }

    private fun save(dir: String, name: String, properties: Properties) =
        properties.store(FileOutputStream(getPath(dir, name).toFile()), "")

    /**
     * Load default settings configuration for server
     */
    fun default(name: String) {
        enableEULA(name)
        defaultSettings(name)
    }
}