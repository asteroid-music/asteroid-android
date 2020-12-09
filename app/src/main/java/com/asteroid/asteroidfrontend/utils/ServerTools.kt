package com.asteroid.asteroidfrontend.utils

import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.models.Response
import com.asteroid.asteroidfrontend.models.ServerModel
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

object ServerTools {

    /**
     * Adds a new server to the specified realm, if valid
     */
    fun addNewServer(realm: Realm, name: String, address: String, local: Boolean, wifiNetworkId: Int? = null): Response {
        when {
            name.isEmpty() -> return Response(false,R.string.server_name_empty_prompt)
            address.isEmpty() -> return Response(false,R.string.server_address_empty_prompt)
            realm.where<ServerModel>().equalTo("name",name)
                .findAll()
                .isNotEmpty() -> return Response(false, R.string.server_name_in_use_prompt)
            (local && wifiNetworkId != null && realm.where<ServerModel>().equalTo("address",address)
                .equalTo("wifiNetworkId", wifiNetworkId)
                .findAll()
                .isNotEmpty()) -> return Response(false,R.string.server_address_in_use_prompt)
            (!local && realm.where<ServerModel>().equalTo("address",address)
                .findAll()
                .isNotEmpty()) -> return Response(false,R.string.server_address_in_use_prompt)
            else -> {
                realm.executeTransaction {
                    val newServerItem = realm.createObject<ServerModel>(name)
                    newServerItem.address = address
                    newServerItem.local = local
                    newServerItem.wifiNetworkId = wifiNetworkId
                }
                return Response(true,0)
            }
        }
    }

}