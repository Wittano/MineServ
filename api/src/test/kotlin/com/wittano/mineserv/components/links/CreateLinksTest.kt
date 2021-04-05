package com.wittano.mineserv.components.links

import com.wittano.mineserv.enums.GlobalRegex
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class CreateLinksTest {

    private val searchLinks = CreateLinks()
    private val versionRegex = GlobalRegex.VERSION.regex
    private val linkRegex = GlobalRegex.LINK.regex

    @Test
    fun loadAllVersion() {
        val list = searchLinks.create()

        assertNotEquals(list.size, 0)
        assertTrue(list.size >= 67)

        list.forEach {
            assertTrue(versionRegex.matches(it.version))
            assertTrue(linkRegex.matches(it.link))
        }
    }

}