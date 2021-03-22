package com.wittano.mineserv.config.scheduling

import com.wittano.mineserv.components.links.CreateLinks
import com.wittano.mineserv.repository.VersionRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Component for insert links to database
 */
@Component
class InsertLinks(
    private val repo: VersionRepository,
    private val createLinks: CreateLinks
) {
    private val logger = LoggerFactory.getLogger(InsertLinks::class.qualifiedName)

    /**
     * Save links to database
     */
    @Scheduled(fixedRate = 86400000)
    fun insert() {
        try {
            repo.findAll().also { list ->
                createLinks.create().filter {
                    !list.contains(it)
                }.also {
                    if (it.isNotEmpty()) {
                        repo.saveAll(it)
                    }
                }
            }

            logger.info("Updated links database")
        } catch (e: Exception) {
            logger.warn(e.message)
        }
    }
}