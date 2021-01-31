package com.asteroid.asteroidfrontend.ui.serverlist

import android.content.Context
import android.net.wifi.WifiManager
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
import com.asteroid.asteroidfrontend.databinding.FragmentAddServerBinding
import com.asteroid.asteroidfrontend.utils.ServerTools
import com.asteroid.asteroidfrontend.utils.displayMessage

class ServerAddFragment: Fragment() {

    private var _binding: FragmentAddServerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddServerBinding.inflate(inflater,container,false)

        val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager?

        context?.let {context ->
            binding.apply{

                //Make the "add server" button act as expected
                addServerButton.setOnClickListener {
                    val realm = ServerTools.getRealmInstance(context)
                    val serverName: String = editTextServerName.text.toString()
                    var serverAddress: String = editTextServerAddress.text.toString()
                    if (!serverAddress.startsWith("http://") && !serverAddress.startsWith("https://")) {
                        serverAddress = "http://".plus(serverAddress)
                    }
                    val serverIsPrivate: Boolean = serverPrivateSwitch.isChecked
                    var serverNetworkId: Int? = null
                    if (serverIsPrivate) {
                        if (wifiManager != null && wifiManager.isWifiEnabled) {
                            serverNetworkId = wifiManager.connectionInfo.networkId
                        }
                    }
                    val result = ServerTools.addNewServer(realm,serverName,serverAddress,serverIsPrivate,serverNetworkId)
                    if (result.success) findNavController().popBackStack()
                    else context.displayMessage(getString(result.messageId))
                }

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
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}