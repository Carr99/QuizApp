package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R


class GenreFragment : Fragment(R.layout.fragment_genre), View.OnClickListener {
    private val args: GenreFragmentArgs by navArgs()
    private var online = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        online = args.online

        view.findViewById<Button>(R.id.sport).setOnClickListener(this)
        view.findViewById<Button>(R.id.history).setOnClickListener(this)
        view.findViewById<Button>(R.id.culture).setOnClickListener(this)
        view.findViewById<Button>(R.id.other).setOnClickListener(this)

    }

    override fun onClick(v: View) {
        val action : NavDirections
        var genre = ""
        when (v.id) {
            R.id.sport -> {
                genre = "Sport"
            }
            R.id.history -> {
                genre = "History"
            }
            R.id.culture -> {
                genre = "Culture"
            }
            R.id.other-> {
                genre = "Other"
            }
        }
        action = GenreFragmentDirections.actionGenreFragmentToGamesFragment(online, genre)
        findNavController().navigate(action)
    }
}