package com.asteroid.asteroidfrontend.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.adapters.ServerListAdapter
import com.asteroid.asteroidfrontend.models.ServerModel
import com.asteroid.asteroidfrontend.utils.ServerTools
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_server_list.*

/**
 * Activity showing a list of servers, and providing an "add server" button
 */
class ServerListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set the server list layout
        setContentView(R.layout.activity_server_list)

        refreshRecyclerView()

        //Make the '+' button redirect to the AddServerActivity
        addServerButton.setOnClickListener{
            val changePageIntent = Intent(this,ServerAddActivity::class.java)
            startActivity(changePageIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        val realm = ServerTools.getRealmInstance(this)

        //Create a vertical linear layout manager and apply it to the recyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //Create an instance of the server list adapter and apply it to the recyclerView
        val adapter = ServerListAdapter(this, realm.where<ServerModel>().findAll())
        recyclerView.adapter = adapter
    }
}