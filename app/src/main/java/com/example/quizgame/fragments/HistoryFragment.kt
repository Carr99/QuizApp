package com.example.quizgame.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.HistoryAdapter
import com.example.quizgame.HistoryModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val db = Firebase.firestore
    private var historyAdapter :HistoryAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list()

    }

    private fun list(){
        val auth = Firebase.auth
        val user = auth.currentUser
        val userID = user?.uid
        if(userID!=null) {
            val query: Query = db.collection("users").document(userID).collection("history").orderBy("date",Query.Direction.DESCENDING)
            val firestoreRecyclerOptions: FirestoreRecyclerOptions<HistoryModel> =
                FirestoreRecyclerOptions.Builder<HistoryModel>()
                    .setQuery(query, HistoryModel::class.java)
                    .build()

            historyAdapter = HistoryAdapter(firestoreRecyclerOptions, this)
            val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
            historyAdapter!!.startListening()
            if (recyclerView != null) {
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = historyAdapter
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        historyAdapter!!.stopListening()
    }
}