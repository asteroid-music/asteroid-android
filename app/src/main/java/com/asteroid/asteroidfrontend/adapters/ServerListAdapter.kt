package com.asteroid.asteroidfrontend.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.models.ServerModel
import kotlinx.android.synthetic.main.server_list_item.view.*

/**
 * Adapter for the RecyclerView representing the server list
 */
class ServerListAdapter(val context: Context, private val serverList: List<ServerModel>):
    RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

    /**
     * The view holder for a single server list item view
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setData(serverInfo: ServerModel?) {
            serverInfo?.let {
                itemView.serverItemTitle.text = serverInfo.name
            }
        }
    }

    /**
     * Method called when a new list item is created
     *
     * @return an empty ViewHolder instance for the list item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.server_list_item, parent, false)
        return ViewHolder(inflatedView)
    }

    /**
     * Gets the number of items in the server list
     *
     * @return the number of items in the server list
     */
    override fun getItemCount(): Int {
        return serverList.size
    }

    /**
     * Provides data to a list item
     *
     * @param holder: the ViewHolder instance for the given list item
     * @param position: the index of the list item
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currServerInfo = serverList[position]
        holder.setData(currServerInfo)
    }

}