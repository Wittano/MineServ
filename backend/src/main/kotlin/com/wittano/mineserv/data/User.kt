package com.wittano.mineserv.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    @JsonView(DefaultView.Companion.External::class)
    val id: Int?,
    @JsonView(DefaultView.Companion.External::class)
    @Column(unique = true, nullable = false)
    val name: String,
    @JsonView(DefaultView.Companion.Internal::class)
    val password: String
)
