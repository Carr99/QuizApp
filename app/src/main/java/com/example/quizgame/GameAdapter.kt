package com.example.quizgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class GameAdapter(options: FirestoreRecyclerOptions<GameModel>, x : Int, online : Boolean) :
    FirestoreRecyclerAdapter<GameModel, GameAdapter.GameAdapterVH>(options) {
    var from = x
    var online = online

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
        holder.rating.rating = model.rating
        holder.linearLayout.setOnClickListener {
            val gameId = snapshots.getSnapshot(position).id
            if (from == 1) {
                if (online) {
                    matchmakingMulti(gameId)
                } else {
                    matchmakingSolo(gameId)
                }
            } else if (from == 2) {
                //TODO: Go to create game with game id passed
            }
        }
    }

    private fun matchmakingSolo(id: String) {
        //TODO: Set up matchmaking solo, pass gameid and active game id
    }

    private fun matchmakingMulti(id: String) {
        //TODO: Set up matchmaking online, pass gameid and active game id
    }

    class GameAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.list_name)
        var creator: TextView = v.findViewById(R.id.list_creator)
        var rating: RatingBar = v.findViewById(R.id.list_rating)
        var linearLayout: LinearLayout = v.findViewById(R.id.list_row)
    }
}