package com.wittano.mineserv.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView
import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    @JsonView(DefaultView.Companion.External::class)
    val id: Int?,
    @Pattern(regexp = "^[A-Za-z]\\\\w{5,}\$")
    @JsonView(DefaultView.Companion.External::class)
    @Column(unique = true, nullable = false)
    val name: String,
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
    @JsonView(DefaultView.Companion.Internal::class)
    val password: String
)
