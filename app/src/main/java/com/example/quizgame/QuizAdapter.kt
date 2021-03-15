package com.example.quizgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class QuizAdapter(options: FirestoreRecyclerOptions<QuizModel>) :
    FirestoreRecyclerAdapter<QuizModel, QuizAdapter.QuizAdapterVH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAdapterVH {
        return QuizAdapterVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.questions,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuizAdapterVH, position: Int, model: QuizModel) {
        holder.question.text = model.question
        holder.answer.text = model.answer
        holder.option1.text = model.option1
        holder.option2.text = model.option2
        holder.option3.text = model.option3
    }

    class QuizAdapterVH(v: View) : RecyclerView.ViewHolder(v) {
        var question: TextView = v.findViewById(R.id.question)
        var answer: TextView = v.findViewById(R.id.answer)
        var option1: TextView = v.findViewById(R.id.option1)
        var option2: TextView = v.findViewById(R.id.option2)
        var option3: TextView = v.findViewById(R.id.option3)
    }
}