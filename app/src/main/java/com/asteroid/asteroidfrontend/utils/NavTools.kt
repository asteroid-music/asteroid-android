package com.asteroid.asteroidfrontend.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.activities.ServerListActivity
import com.asteroid.asteroidfrontend.activities.SongListActivity
import com.asteroid.asteroidfrontend.activities.SongQueueActivity
import com.google.android.material.navigation.NavigationView

object NavTools {

    private fun startInServerActivity(serverName: String?, context: Context, cls: Class<out AppCompatActivity>) {
        val changePageIntent = Intent(context, cls)
        serverName?.let {
            changePageIntent.putExtra("serverName",serverName)
        }
        startActivity(context,changePageIntent,null)
    }

    /**
     * Sets up the navigation bar for an activity
     *
     * @param serverName: the name of the server
     * @param navView: the NavigationView for the activity
     * @param context: the activity context (can pass `this` from an activity)
     * @param currentId: the id of the current menu item
     * @param drawerLayout: the DrawerLayout of the current activity
     * @param thisTabCallback: method to call when the menu item for the current activity is pressed
     */
    fun setupNavBar(serverName: String?, navView: NavigationView, context: Context, currentId: Int, drawerLayout: DrawerLayout, thisTabCallback: () -> Unit) {
        navView.setCheckedItem(currentId)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                currentId -> {
                    thisTabCallback()
                    drawerLayout.closeDrawer(GravityCompat.START,true)
                }
                R.id.navServerList -> startInServerActivity(null,context,ServerListActivity::class.java)
                R.id.navSongList -> startInServerActivity(serverName,context,SongListActivity::class.java)
                R.id.navQueue -> startInServerActivity(serverName,context,SongQueueActivity::class.java)
            }
            true
        }
    }
}