package com.asteroid.asteroidfrontend.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val okHttpBuilder = OkHttpClient.Builder()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpBuilder.build())

    private val retrofit = retrofitBuilder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}