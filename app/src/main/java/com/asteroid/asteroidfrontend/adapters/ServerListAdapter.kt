package com.asteroid.asteroidfrontend.adapters

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.activities.ServerEditActivity
import com.asteroid.asteroidfrontend.activities.ServerListActivity
import com.asteroid.asteroidfrontend.models.HealthCheck
import com.asteroid.asteroidfrontend.models.ServerModel
import com.asteroid.asteroidfrontend.services.ServerStatusInterface
import com.asteroid.asteroidfrontend.services.ServiceBuilder
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.server_delete_confirmation.view.*
import kotlinx.android.synthetic.main.server_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                //Set up server name
                itemView.serverItemTitle.text = it.name

                //Set up server address
                val reducedAddress: String = it.address.removePrefix("http://").removePrefix("https://")
                itemView.serverItemAddress.text = if(reducedAddress.length > 15) reducedAddress.substring(0,12).plus("...") else reducedAddress

                //Set up event handler for edit button
                itemView.editInfoButton.setOnClickListener {
                    val refreshPageIntent = Intent(context,ServerEditActivity::class.java)
                    refreshPageIntent.putExtra("serverName",serverInfo.name)
                    startActivity(context,refreshPageIntent,null)
                }

                //Set up event handler for delete button
                itemView.deleteServerButton.setOnClickListener {
                    val inflatedView: View = LayoutInflater.from(context).inflate(R.layout.server_delete_confirmation,LinearLayout(context),false)

                    val popupWindow = PopupWindow(
                        inflatedView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    )

                    //Event handler for confirming delete
                    inflatedView.deleteYesButton.setOnClickListener {
                        val realm = Realm.getDefaultInstance()
                        val results = realm.where<ServerModel>().equalTo("name",serverInfo.name).findAll()
                        realm.executeTransaction {
                            results.deleteAllFromRealm()
                        }
                        popupWindow.dismiss()
                        val refreshPageIntent = Intent(context,ServerListActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(context,refreshPageIntent,null)
                    }

                    //Event handler for cancelling delete
                    inflatedView.deleteNoButton.setOnClickListener {
                        popupWindow.dismiss()
                    }

                    popupWindow.showAtLocation(itemView.deleteServerButton, Gravity.CENTER,0,0)
                }

                //Query that the server is up
                val healthCheckService = ServiceBuilder.buildService(ServerStatusInterface::class.java)
                val requestCall = healthCheckService.getServerStatus(it.address.plus("/healthcheck"))
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