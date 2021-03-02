package com.wittano.mineserv.repository

import com.wittano.mineserv.data.Link
import org.springframework.data.repository.CrudRepository

interface LinkRepository : CrudRepository<Link, Int>