package com.wittano.mineserv.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Version(
    @Id
    @GeneratedValue
    val id: Long?,
    @Column(unique = true)
    val version: String,
    val link: String
)
