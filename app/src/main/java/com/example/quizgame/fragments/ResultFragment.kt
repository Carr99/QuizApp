package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ResultFragment : Fragment(R.layout.fragment_result) {
    private val args: ResultFragmentArgs by navArgs()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val names = view.findViewById<TextView>(R.id.names)
        val scores = view.findViewById<TextView>(R.id.score)
        val rateGameLayout = view.findViewById<LinearLayout>(R.id.rateGameId)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBarId)
        val facebookButtonLayout = view.findViewById<LinearLayout>(R.id.facebookButton)

        val gameID = args.gameID
        val activeGameID = args.activeGameID

        auth = Firebase.auth
        val user = auth.currentUser?.uid

        ratingBar.setOnRatingBarChangeListener { rating, fl, b ->
            val array = user?.let { db.collection("users").document(it) }
            if (array != null) {
                array.update("ratedGames", FieldValue.arrayUnion(gameID))
                val scoreUpdate = db.collection("Games").document(gameID)
                val score = ratingBar.rating.toLong()
                scoreUpdate.update("score", FieldValue.increment(score))
                scoreUpdate.update("vote", FieldValue.increment(1))
                Toast.makeText(context, "You have voted!", Toast.LENGTH_SHORT).show()
            }
            ratingBar.setIsIndicator(true)
        }

        facebookButtonLayout.setOnClickListener {
            //TODO: Share on facebook
        }

        db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        if (document.getString("player2") != null) {
                            // Played Online
                            val player1 = document.getString("player1")
                            val player2 = document.getString("player2")
                            val player1Score = document.getLong("player1Score")
                            val player2Score = document.getLong("player2Score")
                            val playerScores = "$player1Score - $player2Score"
                            val players = "$player1 vs $player2"
                            names.text = players
                            if (document.getBoolean("player1Finished") == true && document.getBoolean(
                                    "player2Finished"
                                ) == true
                            ) {
                                facebookButtonLayout.visibility = View.VISIBLE
                                scores.text = playerScores
                            } else {
                                scores.text = "Not Finished"
                            }
                        } else {
                            //Played Solo
                            facebookButtonLayout.visibility = View.VISIBLE
                            val shared = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val username = shared?.getString("username", "")
                            if (username != null) {
                                names.text = username
                                val scoreText = document.getLong("player1Score").toString() + " OF 5"
                                scores.text = scoreText
                            }
                        }
                        if (user != null) {
                            db.collection("users").document(user)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val document = task.result
                                        if (document!!.exists()) {
                                            val list =
                                                document.get("ratedGames") as ArrayList<*>
                                            if (list.isNotEmpty()) {
                                                if (!list.contains(gameID)) {
                                                    rateGameLayout.visibility = View.VISIBLE
                                                }
                                            } else {
                                                rateGameLayout.visibility = View.VISIBLE
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
    }
}