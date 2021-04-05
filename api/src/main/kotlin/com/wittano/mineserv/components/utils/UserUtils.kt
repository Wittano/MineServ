package com.wittano.mineserv.components.utils

import com.wittano.mineserv.repository.UserRepository
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class UserUtils(
    private val repo: UserRepository
) {
    fun getUser(principal: Principal) = repo.findByUsername(principal.name)
}