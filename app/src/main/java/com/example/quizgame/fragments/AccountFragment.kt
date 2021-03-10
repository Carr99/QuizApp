package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changePasswordButton = view.findViewById<Button>(R.id.changePasswordButtonId)
        val usernameTextView = view.findViewById<TextInputEditText>(R.id.usernameTextViewId)
        val password = view.findViewById<TextInputEditText>(R.id.password)
        val logOutButton = view.findViewById<Button>(R.id.logOutButtonId)

        val shared = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = shared?.getString("username", "")

        if (username != null){
            usernameTextView.setText(username)
        }

        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        changePasswordButton.setOnClickListener {
            if (password.text!!.length > 5) {
                Firebase.auth.currentUser?.updatePassword(password.text.toString())?.addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "Password change failed", Toast.LENGTH_SHORT).show()
                }

            } else {
                password.error = "Minimum 5 Characters"
            }
        }
    }
}