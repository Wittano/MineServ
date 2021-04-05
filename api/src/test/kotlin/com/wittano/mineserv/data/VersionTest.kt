package com.wittano.mineserv.data

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VersionTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "1.12.2", "1.14.4", "1.2.3", "1.16.4", "1.4", "1.14"
        ]
    )
    fun compareTo_ShouldReturnTrue(version: String) {
        assertTrue(Version(null, "1.16.5", "") > version)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "1.12.2", "1.14.4", "1.6.3", "1.16.4", "1.5.3", "1.4.6"
        ]
    )
    fun compareTo_ShouldReturnFalse(version: String) {
        assertFalse(Version(null, "1.4.5", "") > version)
    }

}