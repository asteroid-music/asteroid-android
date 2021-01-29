package com.asteroid.asteroidfrontend.data.models

data class Song(
    var artist: String,
    var song: String,
    var duration: Number,
    var album: String,
    var _id: String,
    var votes: Int?
)