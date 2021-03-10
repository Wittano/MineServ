package com.wittano.mineserv.repository

import com.wittano.mineserv.data.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?
}