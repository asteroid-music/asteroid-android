package com.asteroid.asteroidfrontend.ui.serverlist

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.HealthCheck
import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.data.remote.ServerAPI
import com.asteroid.asteroidfrontend.data.remote.ServiceBuilder
import com.asteroid.asteroidfrontend.databinding.ServerDeleteConfirmationBinding
import com.asteroid.asteroidfrontend.databinding.ServerListItemBinding
import com.asteroid.asteroidfrontend.ui.server.queue.SongQueueActivity
import io.realm.Realm
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Adapter for the RecyclerView representing the server list
 */
class ServerListAdapter(val context: Context, private val serverList: List<Server>, val navController: NavController):
    RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

    /**
     * The view holder for a single server list item view
     */
    inner class ViewHolder(val binding: ServerListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(serverInfo: Server?) {
            serverInfo?.let {
                //Set up server name
                binding.serverItemTitle.text = it.name

                //Set up server address
                val reducedAddress: String = it.address.removePrefix("http://").removePrefix("https://")
                binding.serverItemAddress.text = reducedAddress

                //Set up default click event handler
                binding.root.setOnClickListener {
                    val refreshPageIntent = Intent(context, SongQueueActivity::class.java)
                    refreshPageIntent.putExtra("serverName",serverInfo.name)
                    startActivity(context,refreshPageIntent,null)
                }

                //Set up event handler for edit button
                binding.editInfoButton.setOnClickListener {
                    //Replace with nav
                    navController.navigate(
                        R.id.action_serverListFragment_to_serverEditFragment,
                        bundleOf("serverName" to serverInfo.name)
                    )
                }

                //Set up event handler for delete button
                binding.deleteServerButton.setOnClickListener {
                    val inflatedBinding = ServerDeleteConfirmationBinding.inflate(
                        LayoutInflater.from(context)
                    )

                    val popupWindow = PopupWindow(
                        inflatedBinding.root,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    )

                    //Event handler for confirming delete
                    inflatedBinding.deleteYesButton.setOnClickListener {
                        val realm = Realm.getDefaultInstance()
                        val results = realm.where<Server>().equalTo("name",serverInfo.name).findAll()
                        realm.executeTransaction {
                            results.deleteAllFromRealm()
                        }
                        popupWindow.dismiss()

                        val refreshPageIntent = Intent(context,
                            ServerListActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(context,refreshPageIntent,null)
                    }

                    //Event handler for cancelling delete
                    inflatedBinding.deleteNoButton.setOnClickListener {
                        popupWindow.dismiss()
                    }

                    popupWindow.showAtLocation(binding.deleteServerButton, Gravity.CENTER,0,0)
                }

                //Query that the server is up
                val serverApi = ServiceBuilder.buildService(ServerAPI::class.java)
                val requestCall = serverApi.getServerStatus(it.address)
                requestCall.enqueue(object: Callback<HealthCheck> {
                    override fun onFailure(call: Call<HealthCheck>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        binding.connectionBadIndicator.visibility = View.VISIBLE
                    }

                    override fun onResponse(
                        call: Call<HealthCheck>,
                        response: Response<HealthCheck>
                    ) {
                        if(response.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            binding.connectionGoodIndicator.visibility = View.VISIBLE
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.connectionBadIndicator.visibility = View.VISIBLE
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
        val binding = ServerListItemBinding
            .inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
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