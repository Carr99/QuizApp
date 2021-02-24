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


class GameAdapter(options: FirestoreRecyclerOptions<GameModel>) :
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
        holder.rating.rating = model.rating
        holder.linearLayout.setOnClickListener {
            val gameId = snapshots.getSnapshot(position).id
            matchmaking(gameId)
        }
    }

    fun matchmaking(id: String) {
        //TODO: Set up matchmaking, depending on offline or online

    }

    class GameAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.list_name)
        var creator: TextView = v.findViewById(R.id.list_creator)
        var rating: RatingBar = v.findViewById(R.id.list_rating)
        var linearLayout: LinearLayout = v.findViewById(R.id.list_row)
    }
}