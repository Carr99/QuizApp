package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.quizgame.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameFragment : Fragment(R.layout.fragment_game) {
    private val db = Firebase.firestore
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load all info

    }


    // save score etc to db
    fun saveToDB(){

    }
}