package com.asteroid.asteroidfrontend.services

import com.asteroid.asteroidfrontend.models.SongModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface SongListInterface {

    @POST
    fun requestSong(@Url url: String, @Query("url") song_url: String): Call<SongModel>

    @GET
    fun getSongList(@Url url: String): Call<List<SongModel>>
}