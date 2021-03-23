package com.wittano.mineserv.components.links

import com.wittano.mineserv.config.scheduling.UpdateLinks
import com.wittano.mineserv.repository.VersionRepository
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UpdateLinksTest {

    @Autowired
    private lateinit var updateLinks: UpdateLinks

    @Autowired
    private lateinit var repo: VersionRepository

    @Test
    fun insertLinks_ShouldReturnLinks() {
        // In others tests use database, so sometimes insert() function can throw exception
        try {
            updateLinks.update()
        } catch (e: Exception) {
        }

        val lists = repo.findAll()

        assertNotNull(lists)
        assertNotEquals(lists.count(), 0)
    }
}