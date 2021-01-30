package com.asteroid.asteroidfrontend.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Server(
    @PrimaryKey var name: String = "",
    var address: String = "",
    var local: Boolean = false,
    var wifiNetworkId: Int? = null
): RealmObject()