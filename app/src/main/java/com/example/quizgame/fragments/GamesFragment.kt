package com.example.quizgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.GameAdapter
import com.example.quizgame.GameModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class GamesFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("Games")
    private var online: Boolean = false
    private val args: GamesFragmentArgs by navArgs()
    var gameAdapter: GameAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_games, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        online = args.online

        setUpRecyclerView()
    }

    fun setUpRecyclerView() {
        val query: Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<GameModel> =
            FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query, GameModel::class.java)
                .build()
        gameAdapter = GameAdapter(firestoreRecyclerOptions, 1, online)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = gameAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        gameAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameAdapter!!.stopListening()
    }
}
