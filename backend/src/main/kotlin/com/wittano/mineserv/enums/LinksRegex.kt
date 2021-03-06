package com.wittano.mineserv.enums

enum class LinksRegex(val regex: Regex) {
    VERSION(Regex("^([\\d]{1})\\.([\\d]{1,2})\\.?([\\d]{1,2})?\$")),
    LINK(Regex("^(https)([:\\/]+)(launcher\\.mojang\\.com)([\\w\\/.:]+)(\\/server\\.jar)\$"))
}