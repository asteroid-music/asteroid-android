package com.asteroid.asteroidfrontend.services

import com.asteroid.asteroidfrontend.models.SongModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface SongListInterface {
    @GET
    fun getSongList(@Url url: String): Call<List<SongModel>>
}