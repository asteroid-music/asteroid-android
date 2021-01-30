package com.asteroid.asteroidfrontend.ui.server.credits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.databinding.ActivityCreditsBinding
import com.asteroid.asteroidfrontend.utils.NavTools

/**
 * Activity for requesting a song
 */
class CreditsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreditsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set up the menu button
        binding.menuButton.setOnClickListener {
            binding.activityCredits.openDrawer(GravityCompat.START,true)
        }

        val serverName: String? = intent.extras?.getString("serverName")
        serverName?.let{binding.toolBar.title = it}

        //Set the navigation menu up
        NavTools.setupNavBar(
            serverName,
            binding.navView,
            this,
            R.id.navCredits,
            binding.activityCredits) {}
    }
}