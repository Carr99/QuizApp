package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.quizgame.R

class ResultFragment : Fragment(R.layout.fragment_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yourName = view.findViewById<TextView>(R.id.yourNameTextView)
        val opponentName = view.findViewById<TextView>(R.id.opponentNameTextView)
        val yourScore = view.findViewById<TextView>(R.id.yourScoreTextView)
        val opponentScore = view.findViewById<TextView>(R.id.opponentScoreTextView)
        val rateGameLayout = view.findViewById<LinearLayout>(R.id.rateGameId)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBarId)
        val facebookButtonLayout = view.findViewById<LinearLayout>(R.id.facebookButton)


    }
}