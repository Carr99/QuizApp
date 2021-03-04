package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        val createBtn = view.findViewById<Button>(R.id.create_button)

        createBtn.setOnClickListener {
            val action = MyGamesFragmentDirections.actionMyGamesFragmentToCreateGameFragment("0")
            findNavController().navigate(action)
        }

        if (user != null) {
            val shared = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val username = shared?.getString("username", "")
            if (username != null) {
                setUpRecyclerView(username)
            }
        }
    }

    private fun setUpRecyclerView(username: String) {
        val query: Query = db.collection("Games").whereEqualTo("creator", username)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<GameModel> =
            FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query, GameModel::class.java)
                .build()
        gameAdapter = GameAdapter(firestoreRecyclerOptions, 2, false, this)
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