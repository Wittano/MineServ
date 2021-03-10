package com.wittano.mineserv.repository

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ServerRepository : CrudRepository<Server, Long> {
    fun findAllByOwner(owner: User): List<Server>

    @Query("select distinct 1 from Server s where s.name = :name")
    fun existsServerByName(@Param("name") name: String): Boolean?
}