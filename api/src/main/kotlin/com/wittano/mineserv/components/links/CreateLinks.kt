package com.wittano.mineserv.components.links

import com.wittano.mineserv.data.Version
import com.wittano.mineserv.enums.LinksRegex
import com.wittano.mineserv.enums.MinecraftServerLinks
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

/**
 * Component, which creates collection of links to download vanilla minecraft server
 */
@Component
class CreateLinks {

    /**
     * Create list of links to minecraft server, which user allow to download
     */
    fun create(): List<Version> {
        val links = AtomicReference<MutableList<Version>>(mutableListOf())
        Executors.newFixedThreadPool(2)
            .invokeAll(mutableSetOf<Callable<Unit>>(
                Callable {
                    createOfficial(links)
                }, Callable {
                    createMinecraftVersions(links)
                }
            ))

        return links.get()
    }

    /**
     * Add link to download minecraft server from the official minecraft server
     * @param collect list, which will be added new link
     */
    private fun createOfficial(collect: AtomicReference<MutableList<Version>>) {
        val aElement = Jsoup.connect(MinecraftServerLinks.OFFICIAL.link)
            .get().body().getElementsByAttributeValue("aria-label", "mincraft version")

        collect.get()
            .add(
                Version(
                    null,
                    aElement.text().substringAfter('.').substringBeforeLast('.'),
                    aElement.attr("href")
                )
            )
    }

    /**
     * Add link to download minecraft server from the
     * @param collect list, which will be added new link
     */
    private fun createMinecraftVersions(collect: AtomicReference<MutableList<Version>>) {
        Jsoup.connect(MinecraftServerLinks.MINECRAFT_VERSIONS.link)
            .get().body().select(".list-group-item.release")
            .forEach {
                val version = it.getElementsByTag("strong").text()
                val link = it.getElementsByTag("a").last().attr("href")

                if (LinksRegex.VERSION.regex.matches(version) && LinksRegex.LINK.regex.matches(link)) {
                    collect.get().add(
                        Version(
                            null,
                            version,
                            link
                        )
                    )
                }
            }
    }
}