package com.wittano.mineserv.components.links

import com.wittano.mineserv.repository.LinkRepository
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class InsertLinksTest {

    @Autowired
    private lateinit var insertLinks: InsertLinks

    @Autowired
    private lateinit var repo: LinkRepository

    @Test
    fun insertLinks_ShouldReturnLinks() {
        insertLinks.insert()

        val lists = repo.findAll()

        assertNotNull(lists)
        assertNotEquals(lists.count(), 0)
    }
}