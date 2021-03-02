package com.wittano.mineserv.data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Link(
    @Id
    @GeneratedValue
    val id: Int?,
    val version: String,
    val link: String
)
