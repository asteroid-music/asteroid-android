package com.asteroid.asteroidfrontend.services

import com.asteroid.asteroidfrontend.models.Message
import com.asteroid.asteroidfrontend.models.QueueModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface QueueInterface {
    @POST
    fun postQueue(@Url url: String, @Query("song_id") song_id: String, @Query("vote") vote: Int): Call<Message>

    @GET
    fun getQueue(@Url url: String): Call<QueueModel>
}