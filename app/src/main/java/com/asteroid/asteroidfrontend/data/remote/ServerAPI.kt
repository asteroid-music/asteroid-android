package com.asteroid.asteroidfrontend.data.remote

import com.asteroid.asteroidfrontend.data.models.HealthCheck
import com.asteroid.asteroidfrontend.data.models.Message
import com.asteroid.asteroidfrontend.data.models.Queue
import com.asteroid.asteroidfrontend.data.models.Song
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API for interaction with a remote server
 */
interface ServerAPI {

    /**
     * POST request to "/queue" endpoint to upvote/downvote a specified song
     *
     * @param baseUrl: the base URL of the server
     * @param song_id: the id key of the song to upvote
     * @param vote: the value to add to the song's vote tally
     *
     * @return a call object with a message denoting the POST status
     */
    @POST("{baseUrl}/queue")
    fun upvoteSong(
        @Path("baseUrl", encoded = true) baseUrl: String,
        @Query("song_id") song_id: String,
        @Query("vote") vote: Int
    ): Call<Message>

    /**
     * GET request to "/queue" endpoint to query the queue of upcoming songs
     *
     * @param baseUrl: the base URL of the server
     * @return a call object with the data of the queue
     */
    @GET("{baseUrl}/queue")
    fun getQueue(@Path("baseUrl", encoded = true) baseUrl: String): Call<Queue>

    /**
     * GET request to "/healthcheck" endpoint to check a server's health
     *
     * @param baseUrl: the base URL of the server
     * @return a call object with the health check of the server
     */
    @GET("{baseUrl}/healthcheck")
    fun getServerStatus(@Path("baseUrl", encoded = true) baseUrl: String): Call<HealthCheck>

    /**
     * POST request to "/music/songs"
     *
     * @param baseUrl: the base URL of the server
     * @param song_url: the URL of the song to request
     * @return a call object with the data of the added song
     */
    @POST("{baseUrl}/music/songs")
    fun requestSong(
        @Path("baseUrl", encoded = true) baseUrl: String,
        @Query("url") song_url: String
    ): Call<Song>

    /**
     * GET request from "/music/songs"
     *
     * @param baseUrl: the base URL of the server
     * @return a call object with the list of all songs on the server
     */
    @GET("{baseUrl}/music/songs")
    fun getSongList(@Path("baseUrl", encoded = true) baseUrl: String): Call<List<Song>>

}