package com.wittano.mineserv.data

import javax.persistence.*

@Entity
data class Server(
    @Id
    @GeneratedValue
    val id: Long?,
    @Column(unique = true, nullable = false)
    val name: String,
    @OneToOne
    val owner: User,
    @OneToOne
    val version: Version,
    var pid: Long?,
)
