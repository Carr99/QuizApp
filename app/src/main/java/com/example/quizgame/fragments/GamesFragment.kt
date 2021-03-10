package com.example.quizgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.GameAdapter
import com.example.quizgame.GameModel
import com.example.quizgame.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class GamesFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var online: Boolean = false
    private var genre: String = "Sport"
    private val collectionReference: Query = db.collection("Games")
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
        genre = args.genre

        setUpRecyclerView(collectionReference.whereEqualTo("genre", genre), 1)

        val searchView = view.findViewById<SearchView>(R.id.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setUpRecyclerView(collectionReference.whereEqualTo("name", searchView.query.toString()), 2)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
    }

    private fun setUpRecyclerView(query: Query, i: Int) {
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<GameModel> =
            FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query.whereEqualTo("ready", true).orderBy("rating", Query.Direction.DESCENDING).limit(10), GameModel::class.java)
                .build()
        if (i == 1) {
            gameAdapter = GameAdapter(firestoreRecyclerOptions, 1, online, this)
            val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
            if (recyclerView != null) {
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = gameAdapter
            }
        } else {
            gameAdapter?.updateOptions(firestoreRecyclerOptions)
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


