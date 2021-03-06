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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val email = view.findViewById<TextInputEditText>(R.id.email)
        val username = view.findViewById<TextInputEditText>(R.id.username)
        val pass1 = view.findViewById<TextInputEditText>(R.id.password1)
        val pass2 = view.findViewById<TextInputEditText>(R.id.password2)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_button)

        confirmBtn.setOnClickListener {
            if (email.text.toString().isNotEmpty() && username.text.toString().isNotEmpty()) {
                if (pass1.text.toString().length > 5 && pass2.text.toString().length > 5) {
                    if (pass1.text.toString() == pass2.text.toString()) {
                        val usersRef: CollectionReference = db.collection("users")
                        val query: Query =
                            usersRef.whereEqualTo("username", username.text.toString())
                        query.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (documentSnapshot in task.result!!) {
                                    val user = documentSnapshot.getString("username")
                                    if (user == username.text.toString()) {
                                        username.error = "Username Taken"
                                        break
                                    }
                                }
                            }
                            if (task.result!!.size() == 0) {
                                activity?.let { it1 ->
                                    auth.createUserWithEmailAndPassword(
                                        email.text.toString(),
                                        pass1.text.toString()
                                    ).addOnCompleteListener(it1) { task ->
                                        if (task.isSuccessful) {
                                            val user = auth.currentUser
                                            if (user != null) {
                                                val name =
                                                    hashMapOf("username" to username.text.toString())
                                                db.collection("users").document(user.uid).set(name)
                                                val action =
                                                    SignUpFragmentDirections.actionSignUpFragmentToMenuFragment()
                                                findNavController().navigate(action)
                                            }
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Authentication failed.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        pass2.error = "Not matching"
                    }
                } else {
                    pass1.error = "Minimum 6 characters"
                }
            } else {
                when {
                    TextUtils.isEmpty(email.text.toString()) -> {
                        email.error = "Required"
                    }
                    TextUtils.isEmpty(username.text.toString()) -> {
                        username.error = "Required"
                    }
                    TextUtils.isEmpty(pass1.text.toString()) -> {
                        pass1.error = "Required"
                    }
                    TextUtils.isEmpty(pass2.text.toString()) -> {
                        pass2.error = "Required"
                    }
                }
            }
        }
    }
}