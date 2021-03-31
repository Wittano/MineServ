package com.wittano.mineserv.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView
import javax.persistence.*

@Entity
data class Server(
    @Id
    @GeneratedValue
    @JsonView(DefaultView.Companion.External::class)
    val id: Long?,
    @Column(unique = true, nullable = false)
    @JsonView(DefaultView.Companion.External::class)
    val name: String,
    @ManyToOne(cascade = [CascadeType.REFRESH])
    @JsonView(DefaultView.Companion.External::class)
    @JoinColumn(name = "owner_id")
    val owner: User,
    @JsonView(DefaultView.Companion.External::class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "version_id", unique = false)
    val version: Version,
    @JsonView(DefaultView.Companion.Internal::class)
    var pid: Long?,
)
