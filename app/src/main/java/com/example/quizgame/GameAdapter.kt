package com.example.quizgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.fragments.GamesFragmentDirections
import com.example.quizgame.fragments.MyGamesFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class GameAdapter(
    options: FirestoreRecyclerOptions<GameModel>,
    private var from: Int,
    private var online: Boolean,
    private val parentFragment: Fragment
) :
    FirestoreRecyclerAdapter<GameModel, GameAdapter.GameAdapterVH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapterVH {
        return GameAdapterVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GameAdapterVH, position: Int, model: GameModel) {
        holder.name.text = model.name
        holder.creator.text = model.creator
        holder.rating.rating = model.score/model.vote
        holder.linearLayout.setOnClickListener {
            val gameID = snapshots.getSnapshot(position).id
            if (from == 1) {
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                db.collection("Games").document(gameID).collection("Quiz").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var count = 0
                            for (document in task.result!!) count++
                            val arrayList: Array<Int> = Array(5) { -1 }
                            for (i in 0..4) {
                                var index = (0 until count).random()
                                while (arrayList.contains(index)) {
                                    index = (0 until count).random()
                                }
                                arrayList[i] = index
                            }
                            if (online) {
                                matchmakingMulti(gameID, db, arrayList)
                            } else {
                                matchmakingSolo(gameID, db, arrayList)
                            }
                        }
                    }
            } else if (from == 2) {
                val action = MyGamesFragmentDirections.actionMyGamesFragmentToCreateGameFragment(
                    gameID
                )
                parentFragment.findNavController().navigate(action)
            }
        }
    }

    private fun matchmakingSolo(gameID: String, db: FirebaseFirestore, arrayList: Array<Int>) {
        //TODO: Set up matchmaking solo, pass gameid and active game id

        val shared =
            parentFragment.activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)  //make global?
        val username = shared?.getString("username", "")

        val activeGame = hashMapOf(
            "player1" to username,
            "player1Score" to 0,
            "questions" to arrayListOf(
                arrayList[0],
                arrayList[1],
                arrayList[2],
                arrayList[3],
                arrayList[4]
            ),
            "date" to FieldValue.serverTimestamp(),
        )
        val newActiveID = db.collection("Games").document(gameID).collection("ActiveGames").document().id
        db.collection("Games").document(gameID).collection("ActiveGames").document(
            newActiveID
        )
            .set(activeGame)
            .addOnSuccessListener {
                Toast.makeText(
                    parentFragment.activity,
                    "Creating game!",
                    Toast.LENGTH_SHORT
                ).show()

                val action = GamesFragmentDirections.actionGamesFragmentToGameFragment(
                    gameID,
                    newActiveID
                )
                parentFragment.findNavController().navigate(action)

            }
            .addOnFailureListener { e ->
                Toast.makeText(parentFragment.activity, "Error", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun matchmakingMulti(gameID: String, db: FirebaseFirestore, arrayList: Array<Int>) {
        var gameExist = false
        var activeGameID = ""
        var alreadyWaiting = false
        val shared =
            parentFragment.activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = shared?.getString("username", "")
        db.collection("Games")
            .document(gameID)
            .collection("ActiveGames")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.exists()) {
                            val status = document.getString("status")
                            if (status == "waiting") {
                                if (document.getString("player1") != username) {
                                    gameExist = true
                                    activeGameID = document.id
                                } else {
                                    alreadyWaiting = true
                                }
                            }
                        }
                    }
                    if (gameExist) {
                        val data = hashMapOf(
                            "player2" to username,
                            "status" to "Active"
                        )
                        db.collection("Games").document(gameID).collection("ActiveGames")
                            .document(activeGameID)
                            .set(data, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(
                                    parentFragment.activity,
                                    "Found and Joining Game",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val action = GamesFragmentDirections.actionGamesFragmentToGameFragment(
                                    gameID,
                                    activeGameID
                                )
                                parentFragment.findNavController().navigate(action)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(parentFragment.activity, "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else if (!alreadyWaiting) {
                        val activeGame = hashMapOf(
                            "player1" to username,
                            "player1Score" to 0,
                            "player2" to "",
                            "player2Score" to 0,
                            "questions" to arrayListOf(
                                arrayList[0],
                                arrayList[1],
                                arrayList[2],
                                arrayList[3],
                                arrayList[4]
                            ),
                            "status" to "waiting",
                            "date" to FieldValue.serverTimestamp(),
                            "player1Finished" to false,
                            "player2Finished" to false
                        )
                        val newActiveID = db.collection("Games").document(gameID).collection("ActiveGames").document().id
                        db.collection("Games").document(gameID).collection("ActiveGames").document(
                            newActiveID
                        )
                            .set(activeGame)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    parentFragment.activity,
                                    "Creating game!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val action = GamesFragmentDirections.actionGamesFragmentToGameFragment(
                                    gameID,
                                    newActiveID
                                )
                                parentFragment.findNavController().navigate(action)

                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(parentFragment.activity, "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else {
                        Toast.makeText(
                            parentFragment.activity,
                            "Cant have two active games of the same game!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    class GameAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.list_name)
        var creator: TextView = v.findViewById(R.id.list_creator)
        var rating: RatingBar = v.findViewById(R.id.list_rating)
        var linearLayout: LinearLayout = v.findViewById(R.id.list_row)
    }
}