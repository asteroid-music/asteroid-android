package com.asteroid.asteroidfrontend.ui.serverlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asteroid.asteroidfrontend.R
import com.asteroid.asteroidfrontend.data.models.Server
import com.asteroid.asteroidfrontend.databinding.FragmentServerListBinding
import com.asteroid.asteroidfrontend.utils.ServerTools
import io.realm.kotlin.where

class ServerListFragment: Fragment() {

    private var _binding: FragmentServerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServerListBinding.inflate(inflater,container,false)

        refreshRecyclerView()

        //Make the '+' button redirect to the AddServerActivity
        binding.addServerButton.setOnClickListener{
            findNavController().navigate(R.id.action_serverListFragment_to_serverAddFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        _binding?.let {
            refreshRecyclerView()
        }
    }

    private fun refreshRecyclerView() {
        context?.let {
            val realm = ServerTools.getRealmInstance(it)

            //Create a vertical linear layout manager and apply it to the recyclerView
            val layoutManager = LinearLayoutManager(it)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.recyclerView.layoutManager = layoutManager

            //Create an instance of the server list adapter and apply it to the recyclerView
            val adapter =
                ServerListAdapter(
                    it,
                    realm.where<Server>().findAll(),
                    findNavController()
                )
            binding.recyclerView.adapter = adapter
        }
    }

}