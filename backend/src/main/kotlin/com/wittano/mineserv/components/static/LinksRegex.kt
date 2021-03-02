package com.wittano.mineserv.components.static

enum class LinksRegex(val regex: Regex) {
    VERSION(Regex("^([\\d]{1})\\.([\\d]{1,2})\\.?([\\d]{1,2})?\$")),
    LINK(Regex("^([\\w.\\/:]+)(server\\.jar)\$"))
}