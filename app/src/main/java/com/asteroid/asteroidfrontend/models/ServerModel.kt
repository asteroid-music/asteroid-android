package com.asteroid.asteroidfrontend.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ServerModel(
    @PrimaryKey var name: String = "",
    var address: String = "",
    var local: Boolean = false,
    var wifiNetworkId: Int? = null
): RealmObject()