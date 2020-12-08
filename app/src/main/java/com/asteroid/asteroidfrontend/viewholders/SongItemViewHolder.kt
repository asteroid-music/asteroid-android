package com.asteroid.asteroidfrontend.viewholders

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.displayMessage
import com.asteroid.asteroidfrontend.models.Message
import com.asteroid.asteroidfrontend.models.SongModel
import com.asteroid.asteroidfrontend.services.QueueInterface
import com.asteroid.asteroidfrontend.services.ServiceBuilder
import kotlinx.android.synthetic.main.song_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.floor

class SongItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setData(serverAddress: String, songInfo: SongModel?, voteCount: Number?=null, voteButtons: Boolean=false, unfolded: Boolean=false) {
        songInfo?.let {
            itemView.songName.text = songInfo.song
            setUnfoldedState(songInfo,unfolded)
            if (!voteButtons) {
                itemView.upvoteButton.visibility = View.GONE
                itemView.downvoteButton.visibility = View.GONE
            }
            if(voteCount == null) {
                itemView.songVoteCount.text = ""
            } else {
                itemView.songVoteCount.text = voteCount.toString()
            }
            itemView.upvoteButton.setOnClickListener {
                val voteService = ServiceBuilder.buildService(QueueInterface::class.java)
                val requestCall = voteService.postQueue(serverAddress.plus("/queue/"),songInfo._id,1)
                requestCall.enqueue(object: Callback<Message> {
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        itemView.context.displayMessage("onFailure")
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
            itemView.downvoteButton.setOnClickListener {
                val voteService = ServiceBuilder.buildService(QueueInterface::class.java)
                val requestCall = voteService.postQueue(serverAddress.plus("/queue/"),songInfo._id,-1)
                requestCall.enqueue(object: Callback<Message> {
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        itemView.context.displayMessage("Failed to downvote song!")
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

    private fun setUnfoldedState(songInfo: SongModel, unfolded: Boolean) {
        val mins = floor(songInfo.duration.toFloat()/60).toInt()
        val secs = songInfo.duration.toInt() - 60*mins
        val durString = mins.toString().plus(":").plus(secs.toString())

        if (unfolded) {
            itemView.songName.isSingleLine = false
            itemView.songArtist.isSingleLine = false
            itemView.songArtist.text = songInfo.artist
            itemView.songAlbum.visibility = View.VISIBLE
            itemView.songAlbum.text = songInfo.album
            itemView.songDuration.visibility = View.VISIBLE
            itemView.songDuration.text = durString
            itemView.foldButton.setImageResource(R.drawable.ic_baseline_unfold_less_24)
            itemView.foldButton.contentDescription = R.string.see_less_info_about_this_song.toString()
            itemView.foldButton.setOnClickListener {
                setUnfoldedState(songInfo, false)
            }
            itemView.setOnClickListener {
                setUnfoldedState(songInfo,false)
            }
        } else {
            itemView.songName.isSingleLine = true
            itemView.songArtist.isSingleLine = true
            itemView.songArtist.text = songInfo.artist.plus(" - ")
                .plus(songInfo.album).plus(" - ").plus(durString)
            itemView.songAlbum.visibility = View.GONE
            itemView.songDuration.visibility = View.GONE
            itemView.foldButton.setImageResource(R.drawable.ic_baseline_unfold_more_24)
            itemView.foldButton.contentDescription = R.string.see_more_info_about_this_song.toString()
            itemView.foldButton.setOnClickListener {
                setUnfoldedState(songInfo, true)
            }
            itemView.setOnClickListener {
                setUnfoldedState(songInfo,true)
            }
        }
    }
}