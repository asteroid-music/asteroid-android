package com.asteroid.asteroidfrontend

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class AsteroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}