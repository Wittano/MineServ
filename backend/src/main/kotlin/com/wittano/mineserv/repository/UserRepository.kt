package com.wittano.mineserv.repository

import com.wittano.mineserv.data.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}