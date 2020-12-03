package com.asteroid.asteroidfrontend.services

import com.asteroid.asteroidfrontend.models.HealthCheck
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ServerStatusInterface {
    @GET
    fun getServerStatus(@Url url: String): Call<HealthCheck>
}