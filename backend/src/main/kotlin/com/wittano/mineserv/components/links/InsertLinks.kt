package com.wittano.mineserv.components.links

import com.wittano.mineserv.repository.LinkRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class InsertLinks(
    private val repo: LinkRepository,
    private val searchLinks: SearchLinks
) {
    @Scheduled(fixedRate = 86400000)
    fun insert() {
        repo.saveAll(searchLinks.search())
    }
}