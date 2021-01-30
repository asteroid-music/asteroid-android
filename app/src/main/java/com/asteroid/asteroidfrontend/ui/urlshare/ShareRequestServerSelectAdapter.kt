package com.asteroid.asteroidfrontend.ui.urlshare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.HealthCheck
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import com.asteroid.asteroidfrontend.data.remote.ServiceBuilder
import com.asteroid.asteroidfrontend.models.ServerModel
import com.asteroid.asteroidfrontend.ui.server.urlrequest.URLRequestActivity
import kotlinx.android.synthetic.main.server_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Adapter for the RecyclerView representing the server list
 */
class ShareRequestServerSelectAdapter(val context: Context, private val serverList: List<ServerModel>, private val requestURL: String?):
    RecyclerView.Adapter<ShareRequestServerSelectAdapter.ViewHolder>() {

    /**
     * The view holder for a single server list item view
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setData(serverInfo: ServerModel?) {
            serverInfo?.let {
                //Set up server name
                itemView.serverItemTitle.text = it.name

                //Set up server address
                val reducedAddress: String = it.address.removePrefix("http://").removePrefix("https://")
                itemView.serverItemAddress.text = reducedAddress

                //Hide edit button
                itemView.editInfoButton.visibility = View.GONE
                //Hide delete button
                itemView.deleteServerButton.visibility = View.GONE

                //Query that the server is up
                val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
                val requestCall = serverApi.getServerStatus(it.address)
                requestCall.enqueue(object: Callback<HealthCheck> {
                    override fun onFailure(call: Call<HealthCheck>, t: Throwable) {
                        itemView.progressBar.visibility = View.GONE
                        itemView.connectionBadIndicator.visibility = View.VISIBLE
                    }

                    override fun onResponse(
                        call: Call<HealthCheck>,
                        response: Response<HealthCheck>
                    ) {
                        if(response.isSuccessful) {
                            itemView.progressBar.visibility = View.GONE
                            itemView.connectionGoodIndicator.visibility = View.VISIBLE

                            //Set up default click event handler
                            itemView.setOnClickListener {
                                val requestIntent = Intent(context, URLRequestActivity::class.java)
                                requestIntent.putExtra("serverName",serverInfo.name)
                                requestURL?.let {
                                    requestIntent.putExtra("requestURL", requestURL)
                                }
                                requestIntent.putExtra("closeAfterRequest",true)
                                ContextCompat.startActivity(context, requestIntent, null)
                            }
                        } else {
                            itemView.progressBar.visibility = View.GONE
                            itemView.connectionBadIndicator.visibility = View.VISIBLE
                        }
                    }

                })
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