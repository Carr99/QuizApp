package com.example.quizgame.fragments

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
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //val textView = view.findViewById<TextView>(R.id.textView)
        //textView.text = "Welcome"
        Handler(Looper.getMainLooper()).postDelayed({
            auth = Firebase.auth
            val user = auth.currentUser

            if (user == null) {
                // User is signed in.
                val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                findNavController().navigate(action)
            } else {
                // No user is signed in.
                val action = HomeFragmentDirections.actionHomeFragmentToMenuFragment()
                findNavController().navigate(action)
            }
        }, 2500)

    }

}