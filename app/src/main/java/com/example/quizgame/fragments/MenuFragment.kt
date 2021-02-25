package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R

class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<Button>(R.id.buttonPlay)
        val profileButton = view.findViewById<Button>(R.id.buttonProfile)

        playButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToOnlineOfflineFragment()
            findNavController().navigate(action)
        }

        profileButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToProfileMenuFragment()
            findNavController().navigate(action)
        }
    }
}