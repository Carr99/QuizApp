package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R

class OnlineOfflineFragment : Fragment(R.layout.fragment_online_or_offline) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val soloButton = view.findViewById<Button>(R.id.buttonSolo)
        val randomButton = view.findViewById<Button>(R.id.buttonRandom)

        soloButton.setOnClickListener {
            val action = OnlineOfflineFragmentDirections.actionOnlineOfflineFragmentToGenreFragment(false)
            findNavController().navigate(action)
        }

        randomButton.setOnClickListener {
            val action = OnlineOfflineFragmentDirections.actionOnlineOfflineFragmentToGenreFragment(true)
            findNavController().navigate(action)
        }
    }
}