package com.asteroid.asteroidfrontend.ui.serverlist

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.databinding.FragmentAddServerBinding
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage
import io.realm.kotlin.where

class ServerEditFragment: Fragment() {

    private var _binding: FragmentAddServerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddServerBinding.inflate(inflater,container,false)

        context?.let {context ->
            binding.apply{
                //Modify the string constants used within
                addServerButton.setText(R.string.update_server_button_text)

                //Make the "public/private info" button act as expected
                privateInfoButton.setOnClickListener {button ->
                    val inflatedView: View = LayoutInflater.from(context)
                        .inflate(
                            R.layout.server_private_state_info,
                            LinearLayout(context),
                            false
                        )
                    PopupWindow(
                        inflatedView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    ).showAtLocation(button, Gravity.CENTER,0,0)
                }

                //Get the bundled pos number
                val serverName: String? = arguments?.getString("serverName")
                serverName?.let {
                    val realm = ServerTools.getRealmInstance(context)
                    realm.where<Server>().equalTo("name",serverName).findFirst()?.let {serverInfo ->
                        //Put current server info into inputs
                        editTextServerAddress.setText(serverInfo.address)
                        editTextServerName.setText(serverInfo.name)
                        serverPrivateSwitch.isChecked = serverInfo.local

                        //Make the "add server" button act as expected
                        addServerButton.setOnClickListener {
                            val newServerName = editTextServerName.text.toString()
                            var serverAddress: String = editTextServerAddress.text.toString()
                            if (
                                !serverAddress.startsWith("http://")
                                && !serverAddress.startsWith("https://")
                            ) {
                                serverAddress = "http://".plus(serverAddress)
                            }
                            val serverIsPrivate: Boolean = serverPrivateSwitch.isChecked
                            val result = ServerTools.updateExistingServer(
                                ServerTools.getRealmInstance(context),
                                serverInfo.name,
                                newServerName,
                                serverAddress,
                                serverIsPrivate,
                                serverInfo.wifiNetworkId
                            )
                            if (result.success) findNavController().popBackStack()
                            else context.displayMessage(getString(result.messageId))
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}