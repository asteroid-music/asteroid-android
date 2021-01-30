package com.asteroid.asteroidfrontend.ui.server.urlrequest

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.Song
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import com.asteroid.asteroidfrontend.data.remote.ServiceBuilder
import com.asteroid.asteroidfrontend.databinding.ActivityUrlRequestBinding
import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.utils.NavTools
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity for requesting a song
 */
class URLRequestActivity : AppCompatActivity() {

    lateinit var binding: ActivityUrlRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityUrlRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolBar)

            //Set up the menu button
            menuButton.setOnClickListener {
                activityURLRequest.openDrawer(GravityCompat.START, true)
            }

            val serverName: String? = intent.extras?.getString("serverName")
            serverName?.let { toolBar.title = it }
            //Set the navigation menu up
            NavTools.setupNavBar(
                serverName,
                navView,
                this@URLRequestActivity,
                R.id.navUrlReq,
                activityURLRequest) {}

            val requestURL = intent.extras?.getString("requestURL")
            requestURL.let {
                editTextRequestURL.setText(requestURL)
            }

            val closeAfterRequest = intent.extras?.getBoolean("closeAfterRequest") == true

            //Set up the request button
            refreshSongListButton.setOnClickListener { button ->
                serverName?.let {
                    val realm = ServerTools.getRealmInstance(this@URLRequestActivity)
                    val serverInfo =
                        realm.where<Server>().equalTo("name", serverName).findFirst()
                    serverInfo?.let {
                        val reqUrl = editTextRequestURL.text.toString()
                        val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
                        val requestCall = serverApi.requestSong(serverInfo.address, reqUrl)
                        val inflatedView: View = LayoutInflater.from(applicationContext)
                            .inflate(R.layout.loading_popup, null)
                        val window = PopupWindow(
                            inflatedView,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            true
                        )
                        window.showAtLocation(button, Gravity.CENTER, 0, 0)
                        requestCall.enqueue(object : Callback<Song> {
                            override fun onFailure(call: Call<Song>, t: Throwable) {
                                window.dismiss()
                                displayMessage("Failed to request song")
                                if (closeAfterRequest) {
                                    finish()
                                }
                            }

                            override fun onResponse(
                                call: Call<Song>,
                                response: Response<Song>
                            ) {
                                window.dismiss()
                                if (response.isSuccessful) {
                                    displayMessage("Successfully requested song")
                                } else {
                                    displayMessage("Failed to request song")
                                }
                                if (closeAfterRequest) {
                                    finish()
                                }
                            }

                        })
                    }
                }
            }
        }
    }
}