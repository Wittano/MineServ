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
) {
    operator fun compareTo(other: String): Int {
        return compareValuesBy(this, other) {
            return compere(this.version.split("."), other.split("."))
        }
    }

    private fun compere(origin: List<String>, other: List<String>, i: Int = 0): Int {
        return when {
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
}
