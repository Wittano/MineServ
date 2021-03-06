package com.wittano.mineserv.repository

import com.wittano.mineserv.data.Version
import org.springframework.data.repository.CrudRepository

interface LinkRepository : CrudRepository<Version, Long> {
    fun findFirstByVersion(version: String): Version?
    fun existsByVersion(version: String): Boolean
}