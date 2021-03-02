package com.wittano.mineserv.components.links

import com.wittano.mineserv.components.static.LinksRegex
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SearchLinksTest {

    private val searchLinks = SearchLinks()
    private val versionRegex = LinksRegex.VERSION.regex
    private val linkRegex = LinksRegex.LINK.regex

    @Test
    fun loadAllVersion() {
        val list = searchLinks.search()

        assertNotEquals(list.size, 0)
        assertTrue(list.size >= 71)

        list.forEach {
            assertTrue(versionRegex.matches(it.version))
            assertTrue(linkRegex.matches(it.link))
        }
    }

}