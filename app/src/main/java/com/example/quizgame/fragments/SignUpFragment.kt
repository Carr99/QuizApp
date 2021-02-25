package com.example.quizgame.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizgame.R
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

        val email = view.findViewById<EditText>(R.id.email)
        val username = view.findViewById<EditText>(R.id.username)
        val pass1 = view.findViewById<EditText>(R.id.password1)
        val pass2 = view.findViewById<EditText>(R.id.password2)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_button)
        val cancelBtn = view.findViewById<Button>(R.id.cancel_button)

        cancelBtn.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        confirmBtn.setOnClickListener {
            if (!TextUtils.isEmpty(email.text.toString()) || !TextUtils.isEmpty(username.text.toString()) || !TextUtils.isEmpty(
                    pass1.text.toString()
                ) || !TextUtils.isEmpty(pass2.text.toString())) {
                if (pass1.text.toString() == pass2.text.toString()) {
                    val usersRef: CollectionReference = db.collection("users")
                    val query: Query = usersRef.whereEqualTo("username", username.text.toString())
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
                                auth.createUserWithEmailAndPassword(email.text.toString(), pass1.text.toString()).addOnCompleteListener(it1) { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        if (user != null) {
                                            val name = hashMapOf("username" to username.text.toString())
                                            db.collection("users").document(user.uid).set(name)
                                        }
                                    } else {
                                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    pass2.error = "Not matching"
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
                        pass1.error = "Minimum 6 characters"
                    }
                    TextUtils.isEmpty(pass2.text.toString()) -> {
                        pass2.error = "Required"
                    }
                }
            }
        }
    }
}