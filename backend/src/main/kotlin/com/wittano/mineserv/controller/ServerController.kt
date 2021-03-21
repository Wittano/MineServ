package com.wittano.mineserv.controller

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.interfaces.service.ServerService
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.batch.item.validator.ValidationException
import org.springframework.batch.item.validator.Validator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/api/server")
class ServerController(
    private val manager: ServerService,
    private val validator: Validator<Server>,
    private val repo: ServerRepository
) {

    private fun managerAction(id: Long, action: (Server) -> Mono<Void>): Mono<ResponseEntity<String>> =
        action(repo.findById(id).orElse(null)).map {
            ResponseEntity.ok().build<String>()
        }.onErrorResume {
            ResponseEntity(it.message, HttpStatus.BAD_REQUEST).toMono()
        }

    @PostMapping("")
    fun create(@RequestBody server: Server): Mono<ResponseEntity<Any>> {
        return try {
            validator.validate(server)

            return manager.create(server).map {
                ResponseEntity(it as Any, HttpStatus.CREATED)
            }.onErrorResume {
                Mono.just(ResponseEntity(it.message, HttpStatus.BAD_REQUEST))
            }
        } catch (e: ValidationException) {
            Mono.just(ResponseEntity(e.message, HttpStatus.BAD_REQUEST))
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): Mono<ResponseEntity<String>> =
        managerAction(id) {
            manager.delete(it)
        }

    @PutMapping("/stop/{id}")
    fun stop(@PathVariable("id") id: Long): Mono<ResponseEntity<String>> =
        managerAction(id) {
            manager.stop(it)
        }

    @PutMapping("/start/{id}")
    fun start(@PathVariable("id") id: Long) =
        managerAction(id) {
            manager.start(it)
        }
}