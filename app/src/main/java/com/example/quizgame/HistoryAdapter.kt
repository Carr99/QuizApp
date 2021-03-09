package com.example.quizgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HistoryAdapter(options: FirestoreRecyclerOptions<HistoryModel>) :
    FirestoreRecyclerAdapter<HistoryModel, HistoryAdapter.HistoryAdapterVH>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapterVH {
        return HistoryAdapterVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.history,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: HistoryAdapterVH, position: Int, model: HistoryModel) {
        holder.titleText.text = model.quizName
        holder.player1Text.text = model.player1
        holder.player2Text.text = model.player2
    }

    class HistoryAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var titleText: TextView = v.findViewById(R.id.title)
        var player1Text: TextView = v.findViewById(R.id.player1)
        var player2Text: TextView = v.findViewById(R.id.player2)
    }
}

