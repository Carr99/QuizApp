package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Handler(Looper.getMainLooper()).postDelayed({

        auth = Firebase.auth
        val user = auth.currentUser


        if (user == null) {
            // User is not signed in.
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            val sharedPreference =
                activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            if (sharedPreference  != null) {
                db.collection("users").document(user.uid).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document!!.exists()) {
                                val username = document["username"] as String
                                val editor = sharedPreference?.edit()
                                editor.putString("username", username)
                                editor.commit()
                                val action = HomeFragmentDirections.actionHomeFragmentToMenuFragment()
                                findNavController().navigate(action)
                            }
                        }
                    }
            }
        }

        }, 2500)
    }
}