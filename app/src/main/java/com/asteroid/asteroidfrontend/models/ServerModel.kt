package com.asteroid.asteroidfrontend.models

data class ServerModel(var name: String, var address: String, var local: Boolean, var wifiNetworkId: Int? = null)

object PlaceholderServerData {
    val servers = mutableListOf (
        ServerModel("Main Server", "http://fake.server.name.com", false),
        ServerModel("Local Server", "http://192.168.0.2:8000", true, 1),
        ServerModel("Third Server", "http://a.different.fake.server.name.com",false)
    )
}