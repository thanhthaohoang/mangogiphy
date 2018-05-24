package com.tthaohoang.mangogiphy.data

import com.giphy.sdk.core.network.api.GPHApiClient

class GiphyManager private constructor()  {

    init {
        println("This ($this) is a singleton for using Giphy client in the all app !")
    }

    companion object {
        var client = GPHApiClient("GTpumr8CiQp2OrwiZTZZHbSjrQ8FEHVs")
    }

}