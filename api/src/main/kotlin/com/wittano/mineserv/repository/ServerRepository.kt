package com.wittano.mineserv.repository

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ServerRepository : CrudRepository<Server, Long> {
    @Query(
        """
           select s from Server s
                inner join User u on s.owner = u
                where u.username = :name
        """
    )
    fun findAllByOwnerName(@Param("name") name: String): List<Server>
    fun findAllByOwner(owner: User): List<Server>
    fun existsByName(name: String): Boolean
}