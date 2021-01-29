package com.asteroid.asteroidfrontend.activities

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.adapters.SongListAdapter
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import com.asteroid.asteroidfrontend.data.remote.ServiceBuilder
import com.asteroid.asteroidfrontend.data.models.Song
import com.asteroid.asteroidfrontend.utils.NavTools
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage
import kotlinx.android.synthetic.main.activity_song_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity showing a list of songs
 */
class SongListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolBar)

        //Set the server list layout
        setContentView(R.layout.activity_song_list)

        //Set up the menu button
        menuButton.setOnClickListener {
            activitySongList.openDrawer(GravityCompat.START,true)
        }

        //Set up the refresh button
        refreshSongListButton.setOnClickListener {
            refreshRecyclerView()
        }

        val serverName: String? = intent.extras?.getString("serverName")
        //Set the navigation menu up
        NavTools.setupNavBar(serverName,navView,this,R.id.navSongList, activitySongList) {
            refreshRecyclerView()
        }

        refreshRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        ServerTools.serverInfoFromIntent(intent.extras,this)?.let{ serverInfo->
            //GET the song list
            val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
            val requestCall = serverApi.getSongList(serverInfo.address)
            activitySongList.post {
                val inflatedView: View =
                    LayoutInflater.from(this).inflate(R.layout.loading_popup, null)
                val window = PopupWindow(
                    inflatedView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
                window.showAtLocation(activitySongList, Gravity.CENTER, 0, 0)
                requestCall.enqueue(object : Callback<List<Song>> {
                    override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                        window.dismiss()
                        displayMessage("Unable to load song list!")
                    }


                    override fun onResponse(
                        call: Call<List<Song>>,
                        response: Response<List<Song>>
                    ) {
                        window.dismiss()
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                //Create a vertical linear layout manager and apply it to the recyclerView
                                val layoutManager =
                                    LinearLayoutManager(this@SongListActivity)
                                layoutManager.orientation = LinearLayoutManager.VERTICAL
                                recyclerView.layoutManager = layoutManager

                                //Create an instance of the server list adapter and apply it to the recyclerView
                                val adapter = SongListAdapter(
                                    serverInfo.address,
                                    this@SongListActivity,
                                    body
                                )
                                recyclerView.adapter = adapter
                            }
                        } else {
                            displayMessage("Unable to load song list!")
                        }
                    }
                })
            }
        }
    }
}