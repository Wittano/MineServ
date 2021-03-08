package com.wittano.mineserv.repository

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import org.springframework.data.repository.CrudRepository

interface ServerRepository : CrudRepository<Server, Long> {
    fun findAllByOwner(owner: User): Set<Server>?
}