package com.asteroid.asteroidfrontend.ui.serverlist

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
import com.asteroid.asteroidfrontend.databinding.ActivityAddServerBinding
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage

/**
 * Activity allowing the user to add a new server
 */
class ServerAddActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddServerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityAddServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        binding.apply{

            //Make the back button act as expected
            backButton.setOnClickListener {
                val changePageIntent = Intent(this@ServerAddActivity, ServerListActivity::class.java)
                startActivity(changePageIntent)
            }

            //Make the "add server" button act as expected
            addServerButton.setOnClickListener {
                val realm = ServerTools.getRealmInstance(this@ServerAddActivity)
                val serverName: String = editTextServerName.text.toString()
                var serverAddress: String = editTextServerAddress.text.toString()
                if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://")) {
                    serverAddress = "http://".plus(serverAddress)
                }
                val serverIsPrivate: Boolean = serverPrivateSwitch.isChecked
                var serverNetworkId: Int? = null
                if (serverIsPrivate) {
                    if (wifiManager.isWifiEnabled) {
                        serverNetworkId = wifiManager.connectionInfo.networkId
                    }
                }
                val result = ServerTools.addNewServer(realm,serverName,serverAddress,serverIsPrivate,serverNetworkId)
                if (result.success) {
                    val changePageIntent = Intent(this@ServerAddActivity, ServerListActivity::class.java)
                    startActivity(changePageIntent)
                } else {
                    displayMessage(getString(result.messageId))
                }
            }

            //Make the "public/private info" button act as expected
            privateInfoButton.setOnClickListener {
                val inflatedView: View = LayoutInflater.from(this@ServerAddActivity)
                    .inflate(
                        R.layout.server_private_state_info,
                        LinearLayout(parent),
                        false
                    )
                PopupWindow(
                    inflatedView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                ).showAtLocation(it, Gravity.CENTER,0,0)
            }
        }
    }
}