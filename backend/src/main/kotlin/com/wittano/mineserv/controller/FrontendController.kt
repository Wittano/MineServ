package com.wittano.mineserv.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Render react frontend
 */
@Controller
class FrontendController {
    @GetMapping("/")
    fun frontend(): String {
        return "index"
    }
}