package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changePasswordButton = view.findViewById<Button>(R.id.changePasswordButtonId)
        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextViewId)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextViewId)
        val logOutButton = view.findViewById<Button>(R.id.logOutButtonId)

        val shared = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = shared?.getString("username", "")

        if (username != null){
            usernameTextView.text = username.toString()
        }

        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}