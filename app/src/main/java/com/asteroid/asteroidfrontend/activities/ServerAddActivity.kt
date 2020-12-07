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
import com.asteroid.asteroidfrontend.models.ServerModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_server.*

/**
 * Activity allowing the user to add a new server
 */
class ServerAddActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        super.onCreate(savedInstanceState)
        //Set the server add layout
        setContentView(R.layout.activity_add_server)

        //Make the "back" button act as expected
        backButton.setOnClickListener {
            val changePageIntent = Intent(this, ServerListActivity::class.java)
            startActivity(changePageIntent)
        }

        //Make the "add server" button act as expected
        addServerButton.setOnClickListener {
            val realm = Realm.getDefaultInstance()
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
            when {
                serverName.isEmpty() -> {
                    displayMessage(getString(R.string.server_name_empty_prompt))
                    serverNetworkId?.let { it1 -> displayMessage(it1.toString()) }
                }
                serverAddress.isEmpty() -> {
                    displayMessage(getString(R.string.server_address_empty_prompt))
                }
                realm.where<ServerModel>().equalTo("name",serverName)
                    .findAll()
                    .isNotEmpty() -> {
                    displayMessage(getString(R.string.server_name_inuse_prompt))
                }
                (serverIsPrivate && serverNetworkId != null && realm.where<ServerModel>().equalTo("address",serverAddress)
                    .equalTo("wifiNetworkId",serverNetworkId)
                    .findAll()
                    .isNotEmpty()) -> {
                    displayMessage(getString(R.string.server_address_inuse_prompt))
                }
                (!serverIsPrivate && realm.where<ServerModel>().equalTo("address",serverAddress)
                    .findAll()
                    .isNotEmpty()) -> {
                    displayMessage(getString(R.string.server_address_inuse_prompt))
                }
                else -> {
                    realm.executeTransaction {
                        val newServerItem = realm.createObject<ServerModel>(serverName)
                        newServerItem.address = serverAddress
                        newServerItem.local = serverIsPrivate
                        newServerItem.wifiNetworkId = serverNetworkId
                    }
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