package com.wittano.mineserv.components.server

import com.wittano.mineserv.components.exceptions.VersionException
import com.wittano.mineserv.config.scheduling.InsertLinks
import com.wittano.mineserv.repository.VersionRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

@SpringBootTest
internal class ServerDownloaderTest {
    @Autowired
    private lateinit var downloader: ServerDownloader

    @Autowired
    private lateinit var createLinks: InsertLinks

    @Autowired
    private lateinit var repo: VersionRepository

    @BeforeEach
    fun beforeEach() {
        try {
            createLinks.insert()
        } catch (e: Exception) {

        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1.16.5", "1.2.3"])
    fun download_ShouldDownloadServerJar(version: String) {
        try {
            downloader.download("test", version)

            val size = Files.size(Path.of("/tmp/test/minecraft_server.${version}.jar"))

            assertNotEquals(0, size)
        } catch (e: FileNotFoundException) {
            fail(e)
        } catch (e: VersionException) {
            assertFalse(repo.existsByVersion(version))
        }
    }

}