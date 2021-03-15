package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreateGameFragment : Fragment(R.layout.fragment_create_game) {
    private val db = Firebase.firestore
    lateinit var radioGroup: RadioGroup
    lateinit var genre: String
    lateinit var gameName: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameName = view.findViewById(R.id.game_name)
        radioGroup = view.findViewById(R.id.radioGenre) // controls checkboxes
        val rad1 = view.findViewById<RadioButton>(R.id.radio1)
        rad1.isChecked = true
        genre = "Sport"
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio1) {
                genre = "Sport"
            } else if (checkedId == R.id.radio2) {
                genre = "Culture"
            } else if (checkedId == R.id.radio3) {
                genre = "History"
            } else if (checkedId == R.id.radio4) {
                genre = "Other"
            }

        }

        val createBtn = view.findViewById<Button>(R.id.create_button)
        val username = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            ?.getString("username", "")


        createBtn.setOnClickListener {
            if (!gameName.text.isNullOrBlank() && !gameName.text.isNullOrEmpty()){
            val newQuiz = hashMapOf(
                "creator" to username.toString(),
                "genre" to genre,
                "name" to gameName.text.toString(),
                "rating" to 0,
                "score" to 0,
                "votes" to 0
            )
            val gameID = db.collection("Games").document().id
            db.collection("Games").document(gameID).set(
                newQuiz
            ).addOnSuccessListener {
                Toast.makeText(context, "Added base", Toast.LENGTH_SHORT).show()
                val action =
                    CreateGameFragmentDirections.actionCreateGameFragmentToCreateQuestionFragment(
                        gameID
                    )
                findNavController().navigate(action)
            }
        }

        }

    }
}