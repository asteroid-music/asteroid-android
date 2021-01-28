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
import com.asteroid.asteroidfrontend.models.QueueModel
import com.asteroid.asteroidfrontend.utils.NavTools
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage
import kotlinx.android.synthetic.main.activity_queue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity showing a list of songs
 */
class SongQueueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolBar)

        //Set the server list layout
        setContentView(R.layout.activity_queue)

        //Set the currently playing text
        currentSong.text = getString(R.string.currently_playing).plus("Unknown")

        //Set up the menu button
        menuButton.setOnClickListener {
            activityQueue.openDrawer(GravityCompat.START,true)
        }

        //Set up the refresh button
        refreshQueueButton.setOnClickListener {
            refreshRecyclerView()
        }

        val serverName: String? = intent.extras?.getString("serverName")
        //Set the navigation menu up
        NavTools.setupNavBar(serverName,navView,this,R.id.navQueue, activityQueue) {
            refreshRecyclerView()
        }

        refreshRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        ServerTools.serverInfoFromIntent(intent.extras,this)?.let{ serverInfo ->
            //GET the queue
            val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
            val requestCall = serverApi.getQueue(serverInfo.address)
            activityQueue.post {
                val inflatedView: View = LayoutInflater.from(this).inflate(R.layout.loading_popup, null)
                val window = PopupWindow(
                    inflatedView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
                window.showAtLocation(activityQueue, Gravity.CENTER,0,0)
                requestCall.enqueue(object: Callback<QueueModel> {
                    override fun onFailure(call: Call<QueueModel>, t: Throwable) {
                        window.dismiss()
                        displayMessage("Unable to load queue!")
                    }


                    override fun onResponse(
                        call: Call<QueueModel>,
                        response: Response<QueueModel>
                    ) {
                        window.dismiss()
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                //Create a vertical linear layout manager and apply it to the recyclerView
                                val layoutManager = LinearLayoutManager(this@SongQueueActivity)
                                layoutManager.orientation = LinearLayoutManager.VERTICAL
                                recyclerView.layoutManager = layoutManager

                                val songList = body.songs.map { item ->
                                    item.song.votes = item.votes
                                    item.song
                                }.sortedByDescending { item -> item.votes }

                                //Create an instance of the server list adapter and apply it to the recyclerView
                                val adapter = SongListAdapter(serverInfo.address, this@SongQueueActivity, songList)
                                recyclerView.adapter = adapter
                            }
                        } else {
                            displayMessage("Unable to load queue!")
                        }
                    }
                })
            }
        }
    }
}