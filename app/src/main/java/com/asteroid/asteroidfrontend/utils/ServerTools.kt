package com.asteroid.asteroidfrontend.utils

import android.content.Context
import android.os.Bundle
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.models.Response
import com.asteroid.asteroidfrontend.models.ServerModel
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

object ServerTools {

    /**
     * Gets information about the current server from the "serverName" extra passed in intent
     *
     * @param bundle: the activity's intent extras
     * @param context: the context to initialise Realm to
     *
     * @returns: the server info if the passed server name exists and is valid, otherwise null
     */
    fun serverInfoFromIntent(bundle: Bundle?, context: Context): ServerModel? {
        bundle?.let {
            val serverName: String? = it.getString("serverName")
            serverName?.let {
                val realm = getRealmInstance(context)
                return realm.where<ServerModel>().equalTo("name",serverName).findFirst()
            }
        }
        return null
    }

    /**
     * Safely gets a Realm default instance, ensuring that
     * Realm.init() is called using a try/catch block
     *
     * @param context: the context to initialise Realm to
     *
     * @return: the default Realm instance
     */
    fun getRealmInstance(context: Context): Realm {
        return try {
            Realm.getDefaultInstance()
        } catch (e: Exception) {
            Realm.init(context)
            Realm.getDefaultInstance()
        }
    }

    /**
     * Adds a new server to the specified realm, if valid
     *
     * @param realm: the realm to add the server to
     * @param name: the name of the new server
     * @param address: the address of the new server
     * @param local: whether the server is local
     * @param wifiNetworkId: if local, the associated wifi network ID
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

    /**
     * Updates the info of a server in specific realm, if valid
     *
     * @param realm: the realm to add the server to
     * @param oldName: the name of the server to update
     * @param newName: the name of the updated server
     * @param address: the address of the updated server
     * @param local: whether the server is local
     * @param wifiNetworkId: if local, the associated wifi network ID
     */
    fun updateExistingServer(realm: Realm, oldName: String, newName: String, address: String, local: Boolean, wifiNetworkId: Int? = null): Response {
        when {
            newName.isEmpty() -> return Response(false, R.string.server_name_empty_prompt)
            address.isEmpty() -> return Response(false, R.string.server_address_empty_prompt)
            realm.where<ServerModel>().equalTo("name",oldName)
                .findAll()
                .isEmpty() -> return Response(false, R.string.server_name_not_recognised)
            (oldName != newName) -> {
                realm.executeTransaction {
                    //Add new object
                    val newServerItem = realm.createObject<ServerModel>(newName)
                    newServerItem.address = address
                    newServerItem.local = local
                    newServerItem.wifiNetworkId = wifiNetworkId
                    //Delete old object
                    realm.where<ServerModel>().equalTo("name",oldName)
                        .findAll()
                        .deleteAllFromRealm()
                }
            }
            else -> {
                realm.executeTransaction {
                    realm.copyToRealmOrUpdate(
                        ServerModel(
                            newName,
                            address,
                            local,
                            wifiNetworkId
                        )
                    )
                }
            }
        }
        return Response(true,0)
    }

}