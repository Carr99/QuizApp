package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.HistoryAdapter
import com.example.quizgame.HistoryModel
import com.example.quizgame.QuizModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val db = Firebase.firestore
    private var historyAdapter :HistoryAdapter? = null
    private val args: HistoryFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared =
            parentFragment?.activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = shared?.getString("username", "")


        list()

    }

    private fun list(){
        val query: Query = db.collection("users").document("T7IguK3iaKbDhqbbkBShwSWbrjI3").collection("history")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<HistoryModel> =
            FirestoreRecyclerOptions.Builder<HistoryModel>()
                .setQuery(query, HistoryModel::class.java)
                .build()
        historyAdapter = HistoryAdapter(firestoreRecyclerOptions)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        historyAdapter!!.startListening()
        if(recyclerView!=null){
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = historyAdapter
        }
        
    }
    override fun onDestroy() {
        super.onDestroy()
        historyAdapter!!.stopListening()
    }
}