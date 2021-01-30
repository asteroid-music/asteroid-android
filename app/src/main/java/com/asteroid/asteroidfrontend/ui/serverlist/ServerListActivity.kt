package com.asteroid.asteroidfrontend.ui.serverlist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asteroid.asteroidfrontend.databinding.ActivityServerListBinding
import com.asteroid.asteroidfrontend.models.ServerModel
import com.asteroid.asteroidfrontend.utils.ServerTools
import io.realm.kotlin.where

/**
 * Activity showing a list of servers, and providing an "add server" button
 */
class ServerListActivity : AppCompatActivity() {

    lateinit var binding: ActivityServerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityServerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        refreshRecyclerView()

        //Make the '+' button redirect to the AddServerActivity
        binding.addServerButton.setOnClickListener{
            val changePageIntent = Intent(this,
                ServerAddActivity::class.java)
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
        binding.recyclerView.layoutManager = layoutManager

        //Create an instance of the server list adapter and apply it to the recyclerView
        val adapter =
            ServerListAdapter(
                this,
                realm.where<ServerModel>().findAll()
            )
        binding.recyclerView.adapter = adapter
    }
}