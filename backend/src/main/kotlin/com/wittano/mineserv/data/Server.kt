package com.wittano.mineserv.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView
import javax.persistence.*

@Entity
data class Server(
    @Id
    @GeneratedValue
    val id: Long?,
    @Column(unique = true, nullable = false)
    val name: String,
    @OneToOne(cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "owner_id")
    val owner: User,
    @OneToOne(optional = false)
    @JoinColumn(name = "version_id")
    val version: Version,
    @JsonView(DefaultView.Companion.Internal::class)
    var pid: Long?,
)
