package com.asteroid.asteroidfrontend.models

data class QueueItem(val votes: Int, val song: SongModel)

data class QueueModel(val name: String, val songs: List<QueueItem>)