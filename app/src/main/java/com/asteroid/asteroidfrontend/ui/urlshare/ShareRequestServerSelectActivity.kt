package com.asteroid.asteroidfrontend.ui.urlshare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.databinding.ActivityServerListBinding
import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.utils.ServerTools
import io.realm.kotlin.where

/**
 * Activity showing a list of servers, and providing an "add server" button
 */
class ShareRequestServerSelectActivity : AppCompatActivity() {

    lateinit var binding: ActivityServerListBinding
    var incomingURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set up the binding
        binding = ActivityServerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get the shared URL
        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            incomingURL = intent.getStringExtra(Intent.EXTRA_TEXT)
        }

        refreshRecyclerView()

        binding.textView.text = getString(R.string.url_share_external_hint)

        //Hide the '+' button
        binding.addServerButton.visibility = View.GONE
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
            ShareRequestServerSelectAdapter(
                this,
                realm.where<Server>().findAll(),
                incomingURL
            )
        binding.recyclerView.adapter = adapter
    }
}