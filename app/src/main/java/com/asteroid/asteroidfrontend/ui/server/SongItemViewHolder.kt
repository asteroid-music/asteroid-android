package com.asteroid.asteroidfrontend.ui.server

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.Message
import com.asteroid.asteroidfrontend.data.models.Song
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import com.asteroid.asteroidfrontend.data.remote.ServiceBuilder
import com.asteroid.asteroidfrontend.databinding.SongListItemBinding
import com.asteroid.asteroidfrontend.utils.displayMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.floor

class SongItemViewHolder(val binding: SongListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun setData(serverAddress: String, songInfo: Song?, voteButtons: Boolean=false, unfolded: Boolean=false) {
        songInfo?.let {
            binding.songName.text = songInfo.song
            setUnfoldedState(songInfo,unfolded)
            if (!voteButtons) {
                binding.upvoteButton.visibility = View.GONE
                binding.downvoteButton.visibility = View.GONE
            }
            if(songInfo.votes == null) {
                binding.songVoteCount.text = ""
            } else {
                binding.songVoteCount.text = songInfo.votes.toString()
            }
            binding.upvoteButton.setOnClickListener {
                val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
                val requestCall = serverApi.upvoteSong(serverAddress,songInfo._id,1)
                songInfo.votes?.let { votes ->
                    binding.songVoteCount.text = (votes + 1).toString()
                }
                requestCall.enqueue(object: Callback<Message> {
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        itemView.context.displayMessage("onFailure")
                        songInfo.votes?.let { votes ->
                            binding.songVoteCount.text = votes.toString()
                        }
                        val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                        circle?.let{_ ->
                            circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#ffffff"),PorterDuff.Mode.MULTIPLY)
                            it.background = circle
                        }
                    }

                    override fun onResponse(
                        call: Call<Message>,
                        response: Response<Message>
                    ) {
                        if (!response.isSuccessful) {
                            songInfo.votes?.let { votes ->
                                binding.songVoteCount.text = votes.toString()
                            }
                            itemView.context.displayMessage("onResponse code ".plus(response.code()))
                            val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                            circle?.let{_ ->
                                circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#ffffff"),PorterDuff.Mode.MULTIPLY)
                                it.background = circle
                            }
                        }
                    }
                })
                val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                circle?.let{_ ->
                    circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#c8e6c9"),PorterDuff.Mode.MULTIPLY)
                    it.background = circle
                }
            }
            binding.downvoteButton.setOnClickListener {
                val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
                val requestCall = serverApi.upvoteSong(serverAddress,songInfo._id,-1)
                songInfo.votes?.let { votes ->
                    binding.songVoteCount.text = (votes - 1).toString()
                }
                requestCall.enqueue(object: Callback<Message> {
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        itemView.context.displayMessage("Failed to downvote song!")
                        songInfo.votes?.let { votes ->
                            binding.songVoteCount.text = votes.toString()
                        }
                        val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                        circle?.let{_ ->
                            circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#ffffff"),PorterDuff.Mode.MULTIPLY)
                            it.background = circle
                        }
                    }

                    override fun onResponse(
                        call: Call<Message>,
                        response: Response<Message>
                    ) {
                        if (!response.isSuccessful) {
                            itemView.context.displayMessage("Failed to downvote song!")
                            songInfo.votes?.let { votes ->
                                binding.songVoteCount.text = votes.toString()
                            }
                            val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                            circle?.let{_ ->
                                circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#ffffff"),PorterDuff.Mode.MULTIPLY)
                                it.background = circle
                            }
                        }
                    }
                })
                val circle: Drawable? = ContextCompat.getDrawable(itemView.context, R.drawable.shape_circle)
                circle?.let{_ ->
                    circle.colorFilter = PorterDuffColorFilter(Color.parseColor("#ffcdd2"),PorterDuff.Mode.MULTIPLY)
                    it.background = circle
                }
            }
        }
    }

    private fun setUnfoldedState(songInfo: Song, unfolded: Boolean) {
        val mins = floor(songInfo.duration.toFloat()/60).toInt()
        val secs = songInfo.duration.toInt() - 60*mins
        val durString = mins.toString().plus(":").plus(secs.toString())

        binding.apply {
            if (unfolded) {
                songName.isSingleLine = false
                songArtist.isSingleLine = false
                songArtist.text = songInfo.artist
                songAlbum.visibility = View.VISIBLE
                songAlbum.text = songInfo.album
                songDuration.visibility = View.VISIBLE
                songDuration.text = durString
                foldButton.setImageResource(R.drawable.ic_baseline_unfold_less_24)
                foldButton.contentDescription = R.string.see_less_info_about_this_song.toString()
                foldButton.setOnClickListener {
                    setUnfoldedState(songInfo, false)
                }
                root.setOnClickListener {
                    setUnfoldedState(songInfo, false)
                }
            } else {
                songName.isSingleLine = true
                songArtist.isSingleLine = true
                songArtist.text = songInfo.artist.plus(" - ")
                    .plus(songInfo.album).plus(" - ").plus(durString)
                songAlbum.visibility = View.GONE
                songDuration.visibility = View.GONE
                foldButton.setImageResource(R.drawable.ic_baseline_unfold_more_24)
                foldButton.contentDescription =
                    R.string.see_more_info_about_this_song.toString()
                foldButton.setOnClickListener {
                    setUnfoldedState(songInfo, true)
                }
                root.setOnClickListener {
                    setUnfoldedState(songInfo, true)
                }
            }
        }
    }
}