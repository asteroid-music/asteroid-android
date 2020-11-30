package com.asteroid.asteroidfrontend.models

data class ServerModel(var name: String)

object PlaceholderServerData {
    val servers = mutableListOf (
        ServerModel("Main Server"),
        ServerModel("Local Server"),
        ServerModel("Third Server")
    )
}