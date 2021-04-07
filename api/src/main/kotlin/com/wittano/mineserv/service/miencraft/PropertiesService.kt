package com.wittano.mineserv.service.miencraft

import com.wittano.mineserv.components.server.ServerProperties
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.util.*
import kotlin.NoSuchElementException

@Service
class PropertiesService(
    private val repo: ServerRepository,
    private val properties: ServerProperties
) {

    /**
     * Get settings from server.properties
     * @param id Server id, from which will be got settings
     * @throws NoSuchElementException throws, when server wasn't found by id
     * @throws FileNotFoundException throws, when server.properties wasn't found
     */
    @Throws(NoSuchElementException::class, FileNotFoundException::class)
    fun getProperties(id: Long): Properties =
        properties.load(repo.findById(id).orElseThrow().name)

    /**
     * Save settings to server.properties
     * @param id Server id, from which will be got settings
     * @param settings New settings for minecraft server
     * @throws NoSuchElementException throws, when server wasn't found by id
     * @throws FileNotFoundException throws, when server.properties wasn't found
     */
    @Throws(NoSuchElementException::class, FileNotFoundException::class)
    fun updateProperties(id: Long, settings: Properties) {
        properties.save(repo.findById(id).orElseThrow().name, properties = settings)
    }
}