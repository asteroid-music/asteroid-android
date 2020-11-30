package com.asteroid.asteroidfrontend

import android.content.Context
import android.widget.Toast

fun Context.displayMessage(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}