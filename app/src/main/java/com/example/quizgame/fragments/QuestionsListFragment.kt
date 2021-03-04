package com.example.quizgame.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.QuizAdapter
import com.example.quizgame.QuizModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class QuestionsListFragment : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var quizAdapter: QuizAdapter? = null
    private val args: QuestionsListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_questions_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<Button>(R.id.add)

        addButton.setOnClickListener {
            val action = QuestionsListFragmentDirections.actionQuestionsListFragmentToCreateQuestionFragment(args.gameId)
            findNavController().navigate(action)

        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query: Query = db.collection("Games").document(args.gameId).collection("Quiz")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<QuizModel> =
            FirestoreRecyclerOptions.Builder<QuizModel>()
                .setQuery(query, QuizModel::class.java)
                .build()
        quizAdapter = QuizAdapter(firestoreRecyclerOptions)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        quizAdapter!!.startListening()
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = quizAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        quizAdapter!!.stopListening()
    }
}