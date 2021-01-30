package com.asteroid.asteroidfrontend.data.repo

import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import io.realm.Realm
import io.realm.kotlin.where
import retrofit2.await
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val serverAPI: ServerAPI,
    private val realm: Realm
): Repository {
    override suspend fun upvoteSong(baseUrl: String, song_id: String, vote: Int)
            = serverAPI.upvoteSong(baseUrl, song_id, vote).await()

    override suspend fun getQueue(baseUrl: String)
            = serverAPI.getQueue(baseUrl).await()

    override suspend fun getServerStatus(baseUrl: String)
            = serverAPI.getServerStatus(baseUrl).await()

    override suspend fun requestSong(baseUrl: String, song_url: String)
            = serverAPI.requestSong(baseUrl,song_url).await()

    override suspend fun getSongList(baseUrl: String)
            = serverAPI.getSongList(baseUrl).await()

    override fun addNewServer(newServer: Server): Boolean {
        realm.executeTransaction {
            it.copyToRealm(newServer)
        }
        return true
    }

    override fun updateExistingServer(newServer: Server): Boolean {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(newServer)
        }
        return true
    }

    override fun removeExistingServer(serverToRemove: Server): Boolean {
        realm.where<Server>().equalTo("name",serverToRemove.name)
            .findAll()
            .deleteAllFromRealm()
        return true
    }
}