package com.wittano.mineserv.data.views

class DefaultView {
    companion object {
        class Internal : External()
        open class External
    }
}