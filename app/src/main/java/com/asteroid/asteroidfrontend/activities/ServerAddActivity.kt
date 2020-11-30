package com.asteroid.asteroidfrontend.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.displayMessage
import com.asteroid.asteroidfrontend.models.PlaceholderServerData
import com.asteroid.asteroidfrontend.models.ServerModel
import kotlinx.android.synthetic.main.activity_add_server.*

/**
 * Activity allowing the user to add a new server
 */
class ServerAddActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set the server add layout
        setContentView(R.layout.activity_add_server)

        //Make the "add server" button act as expected
        addServerButton.setOnClickListener {
            val serverName: String = editTextServerName.text.toString()
            if (serverName.isNotEmpty()) {
                if (ServerModel(serverName) in PlaceholderServerData.servers) {
                    displayMessage("Server name already in use!")
                } else {
                    PlaceholderServerData.servers.add(ServerModel(serverName))
                    val changePageIntent = Intent(this, ServerListActivity::class.java)
                    startActivity(changePageIntent)
                }
            } else {
                displayMessage("Please enter a server name!")
            }
        }
    }
}