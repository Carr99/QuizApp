package com.example.quizgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.GameAdapter
import com.example.quizgame.GameModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase


class MyGamesFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var gameAdapter: GameAdapter? = null
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_games, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            username = document["username"] as String
                            setUpRecyclerView(username)
                        }
                    }
                }
        }
    }

    fun setUpRecyclerView(username: String) {
        val query: Query = db.collection("Games").whereEqualTo("creator", username)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<GameModel> =
            FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query, GameModel::class.java)
                .build()
        gameAdapter = GameAdapter(firestoreRecyclerOptions)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        gameAdapter!!.startListening()
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = gameAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameAdapter!!.stopListening()
    }
}