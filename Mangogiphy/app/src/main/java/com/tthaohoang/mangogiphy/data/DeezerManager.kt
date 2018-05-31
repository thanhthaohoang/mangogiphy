package com.tthaohoang.mangogiphy.data

import com.deezer.sdk.network.connect.DeezerConnect

class DeezerManager private constructor() {
    init {
        println("This ($this) is a singleton for using Deezer connection in the all app !")
    }

    companion object {
        val applicationId = "281402"
        val deezerConnect = DeezerConnect(applicationId)
    }
}