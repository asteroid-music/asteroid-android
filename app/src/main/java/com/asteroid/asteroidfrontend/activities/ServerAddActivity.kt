package com.asteroid.asteroidfrontend.activities

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
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
            var serverAddress: String = editTextServerAddress.text.toString()
            if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://")) {
                serverAddress = "http://".plus(serverAddress)
            }
            val serverIsPrivate: Boolean = serverPrivateSwitch.isChecked
            var serverNetworkId: Int? = null
            if (serverIsPrivate) {
                val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (wifiManager.isWifiEnabled) {
                    serverNetworkId = wifiManager.connectionInfo.networkId
                }
            }
            when {
                serverName.isEmpty() -> {
                    displayMessage("Please enter a server name!")
                    serverNetworkId?.let { it1 -> displayMessage(it1.toString()) }
                }
                serverAddress.isEmpty() -> {
                    displayMessage("Please enter a server address!")
                }
                PlaceholderServerData.servers.any {it.name == serverName} -> {
                    displayMessage("Server name already in use!")
                }
                PlaceholderServerData.servers.any {it.address == serverAddress && (!it.local || it.wifiNetworkId == serverNetworkId)} -> {
                    displayMessage("This server is already in your list!")
                }
                else -> {
                    PlaceholderServerData.servers.add(ServerModel(serverName, serverAddress, serverIsPrivate, serverNetworkId))
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
            ).showAtLocation(it,Gravity.CENTER,0,0)
        }
    }
}