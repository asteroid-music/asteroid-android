package com.asteroid.asteroidfrontend.ui.server.songlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.data.models.Song
import com.asteroid.asteroidfrontend.databinding.SongListItemBinding
import com.asteroid.asteroidfrontend.ui.server.SongItemViewHolder

/**
 * Adapter for the RecyclerView representing the server list
 */
class SongListAdapter(private val serverAddress: String, private val context: Context, private val songList: List<Song>):
    RecyclerView.Adapter<SongItemViewHolder>() {

    /**
     * Method called when a new list item is created
     *
     * @return an empty ViewHolder instance for the list item
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        val binding = SongListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SongItemViewHolder(binding)
    }

    /**
     * Gets the number of items in the server list
     *
     * @return the number of items in the server list
     */
    override fun getItemCount(): Int {
        return songList.size
    }

    /**
     * Provides data to a list item
     *
     * @param holder: the ViewHolder instance for the given list item
     * @param position: the index of the list item
     */
    override fun onBindViewHolder(holder: SongItemViewHolder, position: Int) {
        val currSongInfo = songList[position]
        holder.setData(serverAddress,currSongInfo,voteButtons = true)
    }

}