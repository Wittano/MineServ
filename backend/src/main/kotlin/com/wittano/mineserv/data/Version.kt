package com.wittano.mineserv.data

import com.fasterxml.jackson.annotation.JsonView
import com.wittano.mineserv.data.views.DefaultView
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Version(
    @Id
    @GeneratedValue
    @JsonView(DefaultView.Companion.External::class)
    val id: Long?,
    @Column(unique = true)
    @JsonView(DefaultView.Companion.External::class)
    val version: String,
    @JsonView(DefaultView.Companion.External::class)
    val link: String
) {
    override fun equals(other: Any?): Boolean =
        other is Version &&
                this.link == other.link &&
                this.version == other.version

    operator fun compareTo(other: String): Int =
        compere(this.version.split("."), other.split("."))

    private fun compere(origin: List<String>, other: List<String>, i: Int = 0): Int = when {
        origin[i].toInt() > other[i].toInt() -> {
            1
        }
        origin[i].toInt() < other[i].toInt() -> {
            -1
        }
        i == 2 -> {
            compareValues(origin[i], other[i])
        }
        else -> {
            compere(origin, other, i + 1)
        }
    }
}
