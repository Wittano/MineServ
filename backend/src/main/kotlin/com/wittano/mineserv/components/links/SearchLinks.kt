package com.wittano.mineserv.components.links

import com.wittano.mineserv.components.static.LinksRegex
import com.wittano.mineserv.components.static.MinecraftServerLinks
import com.wittano.mineserv.data.Link
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

@Component
class SearchLinks {

    fun search(): List<Link> {
        val links = AtomicReference<MutableList<Link>>(mutableListOf())
        Executors.newFixedThreadPool(2).invokeAll(mutableSetOf<Callable<Unit>>(
            Callable {
                searchOfficial(links)
            }, Callable {
                searchMinecraftVersions(links)
            }
        ))

        return links.get()
    }

    private fun searchOfficial(collect: AtomicReference<MutableList<Link>>) {
        val aElement = Jsoup.connect(MinecraftServerLinks.OFFICIAL.link)
            .get().body().getElementsByAttributeValue("aria-label", "mincraft version")

        collect.get()
            .add(
                Link(
                    null,
                    aElement.text().substringAfter('.').substringBeforeLast('.'),
                    aElement.attr("href")
                )
            )
    }

    private fun searchMinecraftVersions(collect: AtomicReference<MutableList<Link>>) {
        val versionRegex = LinksRegex.VERSION.regex
        val linkRegex = LinksRegex.LINK.regex

        Jsoup.connect(MinecraftServerLinks.MINECRAFT_VERSIONS.link)
            .get().body().select(".list-group-item.release")
            .forEach {
                val version = it.getElementsByTag("strong").text()
                val link = it.getElementsByTag("a").last().attr("href")

                if (versionRegex.matches(version) && linkRegex.matches(link)) {
                    collect.get().add(
                        Link(
                            null,
                            version,
                            link
                        )
                    )
                }
            }
    }
}