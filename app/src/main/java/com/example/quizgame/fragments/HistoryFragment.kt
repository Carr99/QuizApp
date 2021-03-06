package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.quizgame.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val db = Firebase.firestore


    // method saveHistory in GameAdapter saves to User -> History, either save what we want to show or just save the gameID and collect from ActiveGames / finished games if we add another collection.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared =
            parentFragment?.activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = shared?.getString("username", "")



        val historyRef = db.collection("Games").document("UIRKCVPurTLl0LCvUrjn").collection("ActiveGames").whereEqualTo("player1",username).get().addOnSuccessListener { documents ->
            for (document in documents){
                Log.e("docu",document["player1"].toString())
                Log.e("score",document["player1Score"].toString())
            }
            Log.e("done","done")
        }
        val historyRef2 = db.collection("Games").whereEqualTo("player2",username)


        // val historyReference = db.collection("Games").whereEqualTo("player1",INSERT USERNAME HERE) ------- might need to go through multiple collections to get here
        // same thing again but with player2

        // print these games to the list --- color green if win / red if lose?
        // clicking brings up result fragment with stats for that game


    }
}