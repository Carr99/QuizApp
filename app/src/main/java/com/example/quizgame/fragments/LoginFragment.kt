package com.example.quizgame.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = Firebase.auth

        val email = view.findViewById<TextInputEditText>(R.id.email_text)
        val pass = view.findViewById<TextInputEditText>(R.id.password_text)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        val signUpButton = view.findViewById<Button>(R.id.signUp_button)

        loginButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = pass.text.toString()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                activity?.let { it1 ->
                    auth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToMenuFragment()
                                findNavController().navigate(action)
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } else {
                when {
                    TextUtils.isEmpty(emailText) -> {
                        email.error = "Required"
                    }
                    TextUtils.isEmpty(passwordText) -> {
                        pass.error = "Required"
                    }
                    else -> {
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        signUpButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }
}