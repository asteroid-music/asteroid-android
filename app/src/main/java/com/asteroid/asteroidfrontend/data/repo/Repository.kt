package com.asteroid.asteroidfrontend.data.repo

import com.asteroid.asteroidfrontend.data.models.HealthCheck
import com.asteroid.asteroidfrontend.data.models.Message
import com.asteroid.asteroidfrontend.data.models.Queue
import com.asteroid.asteroidfrontend.data.models.Song

/**
 * Interface for data-access repository.
 * The repository for the app will implement this interface,
 * as well as test double repository
 */
interface Repository {

    /**
     * Upvote/downvote a specified song
     *
     * @param baseUrl: the base URL of the server
     * @param song_id: the id key of the song to upvote
     * @param vote: the value to add to the song's vote tally
     *
     * @return a message denoting the success or failure of the upvote
     */
    suspend fun upvoteSong(
        baseUrl: String,
        song_id: String,
        vote: Int
    ): Message

    /**
     * Retrieve the songs in the queue of a specified server
     *
     * @param baseUrl: the base URL of the server
     * @return the queue data
     */
    suspend fun getQueue(baseUrl: String): Queue

    /**
     * Health-check a specified server
     *
     * @param baseUrl: the base URL of the server
     * @return the health check of the server
     */
    suspend fun getServerStatus(baseUrl: String): HealthCheck

    /**
     * Request that a song with given URL be added to the specified server
     *
     * @param baseUrl: the base URL of the server
     * @param song_url: the URL of the song to request
     * @return the data of the added song
     */
    suspend fun requestSong(
        baseUrl: String,
        song_url: String
    ): Song

    /**
     * Request the list of all songs on a specified server
     *
     * @param baseUrl: the base URL of the server
     * @return the list of all songs on the server
     */
    suspend fun getSongList(baseUrl: String): List<Song>

    /**
     * Add a new server to the specified realm
     *
     * @param name: the name of the new server
     * @param address: the address of the new server
     * @param local: whether the server is local
     * @param wifiNetworkId: if local, the associated wifi network ID
     */
    fun addNewServer(name: String, address: String, local: Boolean, wifiNetworkId: Int? = null): Boolean

    /**
     * Update the info of a server in specific realm
     *
     * @param oldName: the name of the server to update
     * @param newName: the name of the updated server
     * @param address: the address of the updated server
     * @param local: whether the server is local
     * @param wifiNetworkId: if local, the associated wifi network ID
     */
    fun updateExistingServer(oldName: String, newName: String, address: String, local: Boolean, wifiNetworkId: Int? = null): Boolean
}