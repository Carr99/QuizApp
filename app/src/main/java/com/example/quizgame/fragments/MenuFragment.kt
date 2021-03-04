package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MenuFragment : Fragment(R.layout.fragment_menu) {
    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<Button>(R.id.buttonPlay)
        val gamesButton = view.findViewById<Button>(R.id.buttonGames)
        val accountButton = view.findViewById<Button>(R.id.buttonAccount)
        val historyButton = view.findViewById<Button>(R.id.buttonHistory)

        auth = Firebase.auth
        val user = auth.currentUser
        if (user != null) {
            val sharedPreference =
                activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            if (sharedPreference != null) {
                db.collection("users").document(user.uid).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document!!.exists()) {
                                val username = document["username"] as String
                                val editor = sharedPreference.edit()
                                editor.putString("username", username)
                                editor.apply()
                            }
                        }
                    }
            }
        }

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