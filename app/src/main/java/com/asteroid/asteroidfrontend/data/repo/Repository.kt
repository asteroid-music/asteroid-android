package com.asteroid.asteroidfrontend.data.repo

import com.asteroid.asteroidfrontend.data.models.*

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
     * Add a new server to the stored list of servers
     *
     * @param newServer: the server to add
     */
    fun addNewServer(newServer: Server): Boolean

    /**
     * Update the info of a server by primary key
     *
     * @param newServer: the server to update
     */
    fun updateExistingServer(newServer: Server): Boolean

    /**
     * Remove a pre-existing server from the server list
     *
     * @param serverToRemove: the server to remove
     */
    fun removeExistingServer(serverToRemove: Server): Boolean
}