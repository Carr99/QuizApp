package com.example.quizgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateQuestionFragment : Fragment(R.layout.fragment_create_question) {
    private val args: CreateQuestionFragmentArgs by navArgs()
    private val db = Firebase.firestore
    lateinit var questionText: EditText
    lateinit var answerText: EditText
    lateinit var option1: EditText
    lateinit var option2: EditText
    lateinit var option3: EditText
    lateinit var addQuestionButton: Button
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_create_question, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionText = view.findViewById(R.id.questionText)
        answerText = view.findViewById(R.id.answerText)
        option1 = view.findViewById(R.id.option1Text)
        option2 = view.findViewById(R.id.option2Text)
        option3 = view.findViewById(R.id.option3Text)
        addQuestionButton = view.findViewById(R.id.addQuestionButton)
        val gameID = args.gameID


        db.collection("Games").document(gameID).collection("Quiz").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        count++
                    }

                    addQuestionButton.setOnClickListener {
                        val newQuiz = hashMapOf(
                            "question" to questionText.text.toString(),
                            "answer" to answerText.text.toString(),
                            "option1" to option1.text.toString(),
                            "option2" to option2.text.toString(),
                            "option3" to option3.text.toString()
                        )
                        db.collection("Games").document(gameID).collection("Quiz").document(
                            count.toString()
                        )
                            .set(newQuiz)
                            .addOnSuccessListener {
                                Toast.makeText(context,"Added question",Toast.LENGTH_SHORT).show()
                                questionText.text.clear()
                                answerText.text.clear()
                                option1.text.clear()
                                option2.text.clear()
                                option3.text.clear()
                                count++
                            }
                        val readyStatus = hashMapOf(
                            "ready" to true
                        )

                        if(count >= 4) db.collection("Games").document(gameID).set(readyStatus,
                            SetOptions.merge())
                    }


                }
            }
    }


}