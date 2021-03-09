package com.example.quizgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.fragments.HistoryFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class HistoryAdapter(options: FirestoreRecyclerOptions<HistoryModel>,private val parentFragment: Fragment) :
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
        holder.dateText.text = model.date.toString()
        holder.rowHistory.setOnClickListener{
            val action = model.activeGameID?.let { it1 ->
                model.gameID?.let { it2 ->
                    HistoryFragmentDirections.actionHistoryFragmentToResultFragment(
                        it2,
                        it1
                    )
                }
            }
            if (action != null) {
                parentFragment.findNavController().navigate(action)
            }
        }
    }

    class HistoryAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var titleText: TextView = v.findViewById(R.id.title)
        var dateText: TextView = v.findViewById(R.id.dateText)
        var rowHistory: LinearLayout = v.findViewById(R.id.rowHistory)
    }
}

