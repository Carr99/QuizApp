package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreateGameFragment : Fragment(R.layout.fragment_create_game) {
    private val args: CreateGameFragmentArgs by navArgs()
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = args.gameId

        if (gameId != "0") {
            val updateBtn = view.findViewById<Button>(R.id.update_button)
            val questionsBtn = view.findViewById<Button>(R.id.questions_button)

            updateBtn.visibility = View.VISIBLE
            questionsBtn.visibility = View.VISIBLE

            questionsBtn.setOnClickListener {
                val action = CreateGameFragmentDirections.actionCreateGameFragmentToQuestionsListFragment(gameId)
                findNavController().navigate(action)
            }
        } else {
            val createBtn = view.findViewById<Button>(R.id.create_button)
            createBtn.visibility = View.VISIBLE

        }
    }
}