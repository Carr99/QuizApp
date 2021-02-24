package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R

class ProfileMenuFragment : Fragment(R.layout.fragment_profile_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accountButton = view.findViewById<Button>(R.id.buttonAccount)
        val gamesButton = view.findViewById<Button>(R.id.buttonGames)
        val historyButton = view.findViewById<Button>(R.id.buttonHistory)

        accountButton.setOnClickListener {
            val action = ProfileMenuFragmentDirections.actionProfileMenuFragmentToAccountFragment()
            findNavController().navigate(action)
        }

        historyButton.setOnClickListener {
            val action = ProfileMenuFragmentDirections.actionProfileMenuFragmentToHistoryFragment()
            findNavController().navigate(action)
        }

        gamesButton.setOnClickListener {
            //insert game fragment
        }

    }
}