package com.wittano.mineserv.config.scheduling

import com.wittano.mineserv.components.links.CreateLinks
import com.wittano.mineserv.repository.LinkRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.SQLException

/**
 * Component for insert links to database
 */
@Component
class InsertLinks(
    private val repo: LinkRepository,
    private val createLinks: CreateLinks
) {
    private val logger = LoggerFactory.getLogger(InsertLinks::class.simpleName)

    /**
     * Save links to database
     */
    @Scheduled(fixedRate = 86400000)
    fun insert() {
        try {
            val list = repo.findAll()

            repo.saveAll(createLinks.create().filter {
                !list.contains(it)
            })
        } catch (e: SQLException) {
            logger.warn(e.message)
        }
    }
}