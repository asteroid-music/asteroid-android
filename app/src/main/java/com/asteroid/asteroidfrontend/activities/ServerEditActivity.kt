package com.asteroid.asteroidfrontend.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.displayMessage
import com.asteroid.asteroidfrontend.models.PlaceholderServerData
import com.asteroid.asteroidfrontend.models.ServerModel
import kotlinx.android.synthetic.main.activity_add_server.*

/**
 * Activity allowing the user to edit an existing server
 */
class ServerEditActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set the server add layout
        setContentView(R.layout.activity_add_server)
        //Modify the string constants used within
        textView.setText(R.string.edit_server_screen_header)
        addServerButton.setText(R.string.update_server_button_text)

        //Get the intent pos number
        intent.extras?.let {
            val posNumber: Int = it.getInt("pos")
            val serverInfo = PlaceholderServerData.servers[posNumber]

            //Put current server info into inputs
            editTextServerAddress.setText(serverInfo.address)
            editTextServerName.setText(serverInfo.name)
            serverPrivateSwitch.isChecked = serverInfo.local

            //Make the "add server" button act as expected
            addServerButton.setOnClickListener {
                val serverName: String = editTextServerName.text.toString()
                var serverAddress: String = editTextServerAddress.text.toString()
                if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://")) {
                    serverAddress = "http://".plus(serverAddress)
                }
                val serverIsPrivate: Boolean = serverPrivateSwitch.isChecked
                when {
                    serverName.isEmpty() -> {
                        displayMessage("Please enter a server name!")
                    }
                    serverAddress.isEmpty() -> {
                        displayMessage("Please enter a server address!")
                    }
                    else -> {
                        PlaceholderServerData.servers[posNumber] = ServerModel(serverName, serverAddress, serverIsPrivate, serverInfo.wifiNetworkId)
                        val changePageIntent = Intent(this, ServerListActivity::class.java)
                        startActivity(changePageIntent)
                    }
                }
            }

            //Make the "public/private info" button act as expected
            privateInfoButton.setOnClickListener {
                val inflatedView: View = LayoutInflater.from(applicationContext).inflate(R.layout.server_private_state_info,LinearLayout(parent),false)
                PopupWindow(
                    inflatedView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                ).showAtLocation(privateInfoButton,Gravity.CENTER,0,0)
            }
        }
    }
}