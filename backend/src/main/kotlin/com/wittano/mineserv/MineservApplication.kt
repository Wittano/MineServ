package com.wittano.mineserv

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MineservApplication

fun main(args: Array<String>) {
    runApplication<MineservApplication>(*args)
}
