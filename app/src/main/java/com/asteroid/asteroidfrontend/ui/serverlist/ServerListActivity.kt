package com.asteroid.asteroidfrontend.ui.serverlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asteroid.asteroidfrontend.databinding.ActivityServerListBinding

/**
 * Activity showing a list of servers, and providing an "add server" button
 */
class ServerListActivity : AppCompatActivity() {

    lateinit var binding: ActivityServerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityServerListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
    }
}