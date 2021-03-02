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
        val gamesButton = view.findViewById<Button>(R.id.buttonGames)
        val accountButton = view.findViewById<Button>(R.id.buttonAccount)
        val historyButton = view.findViewById<Button>(R.id.buttonHistory)

        playButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToOnlineOfflineFragment()
            findNavController().navigate(action)
        }

        gamesButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToMyGamesFragment()
            findNavController().navigate(action)
        }
        accountButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToAccountFragment()
            findNavController().navigate(action)
        }

        historyButton.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToHistoryFragment()
            findNavController().navigate(action)
        }

    }
}