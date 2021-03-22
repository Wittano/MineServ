package com.wittano.mineserv.controller.data

import com.wittano.mineserv.data.Version
import com.wittano.mineserv.repository.VersionRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/version")
class VersionDataController(
    private val repo: VersionRepository
) {
    @GetMapping("")
    fun getVersions(): Flux<Version> = Flux.fromIterable(repo.findAll())
}