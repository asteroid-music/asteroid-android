package com.asteroid.asteroidfrontend

import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger-hilt application-level module for dependency injection.
 * Provides the Realm database and retrofit instance
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideServerAPI() = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()
        .create(ServerAPI::class.java)

    @Provides
    fun provideRealm() = Realm.getDefaultInstance()
}