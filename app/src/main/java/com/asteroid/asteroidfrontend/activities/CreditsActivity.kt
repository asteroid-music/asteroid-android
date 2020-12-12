package com.asteroid.asteroidfrontend.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.utils.NavTools
import kotlinx.android.synthetic.main.activity_credits.*

/**
 * Activity for requesting a song
 */
class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolBar)

        //Set the server list layout
        setContentView(R.layout.activity_credits)

        //Set up the menu button
        menuButton.setOnClickListener {
            activityCredits.openDrawer(GravityCompat.START,true)
        }

        val serverName: String? = intent.extras?.getString("serverName")
        serverName?.let{toolBar.title = it}
        //Set the navigation menu up
        NavTools.setupNavBar(serverName,navView,this,R.id.navCredits, activityCredits) {
        }
    }
}