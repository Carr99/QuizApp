package com.example.quizgame.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizgame.QuizModel
import com.example.quizgame.R
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GameFragment : Fragment(R.layout.fragment_game), View.OnClickListener {
    private val db = Firebase.firestore
    private lateinit var questionText: TextView
    private lateinit var buttonOne: Button
    private lateinit var buttonTwo: Button
    private lateinit var buttonThree: Button
    private lateinit var buttonFour: Button
    private val args: GameFragmentArgs by navArgs()
    private var score = 0
    var gameID = ""
    var activeGameID = ""
    var solo = true
    private val quiz = arrayListOf<QuizModel>()
    var listOfButtons = arrayListOf<Button>()
    var round = 0
    var questionList = arrayListOf<Long>()
    var gameFinished = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionText = view.findViewById(R.id.questionTextView)
        buttonOne = view.findViewById(R.id.buttonOne)
        buttonTwo = view.findViewById(R.id.buttonTwo)
        buttonThree = view.findViewById(R.id.buttonThree)
        buttonFour = view.findViewById(R.id.buttonFour)

        buttonOne.setOnClickListener(this)
        buttonTwo.setOnClickListener(this)
        buttonThree.setOnClickListener(this)
        buttonFour.setOnClickListener(this)
        gameID = args.gameID
        activeGameID = args.activeGameID
        solo = args.solo

        getQuizQuestions()

    }

    override fun onClick(v: View?) {
        for (a in 0..3) {
            listOfButtons[a].isClickable = false
        }

        if (v == listOfButtons[0]) {
            score++
        }
        round++
        if (round == 5) {
            gameFinished = true
            saveToDB(1)
        } else {
            showQuestions(round)
            for (a in 0..3) {
                listOfButtons[a].isClickable = true
            }
        }
    }

    private fun getQuizQuestions() {
        db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        questionList = (document["questions"] as ArrayList<Long>)
                        db.collection("Games").document(gameID).collection("Quiz").document(questionList[0].toString()).
                        get().addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                val document2 = task2.result
                                if (document2!!.exists())
                                    document2.toObject(QuizModel::class.java)?.let { it ->
                                        quiz.add(it)
                                        db.collection("Games").document(gameID).collection("Quiz").document(questionList[1].toString()).
                                        get().addOnCompleteListener { task3 ->
                                            if (task3.isSuccessful) {
                                                val document3 = task3.result
                                                if (document3!!.exists())
                                                    document3.toObject(QuizModel::class.java)?.let { it2 ->
                                                        quiz.add(it2)
                                                        db.collection("Games").document(gameID).collection("Quiz").document(questionList[2].toString()).
                                                        get().addOnCompleteListener { task4 ->
                                                            if (task4.isSuccessful) {
                                                                val document4 = task4.result
                                                                if (document4!!.exists())
                                                                    document4.toObject(QuizModel::class.java)?.let { it3 ->
                                                                        quiz.add(it3)
                                                                        db.collection("Games").document(gameID).collection("Quiz").document(questionList[3].toString()).
                                                                        get().addOnCompleteListener { task5 ->
                                                                            if (task.isSuccessful) {
                                                                                val document5 = task5.result
                                                                                if (document5!!.exists())
                                                                                    document5.toObject(QuizModel::class.java)?.let { it4 ->
                                                                                        quiz.add(it4)
                                                                                        db.collection("Games").document(gameID).collection("Quiz").document(questionList[4].toString()).
                                                                                        get().addOnCompleteListener { task6 ->
                                                                                            if (task.isSuccessful) {
                                                                                                val document6 = task6.result
                                                                                                if (document6!!.exists())
                                                                                                    document6.toObject(QuizModel::class.java)?.let { it5 ->
                                                                                                        quiz.add(it5)
                                                                                                        showQuestions(0)
                                                                                                    }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                            }
                                                                        }
                                                                    }
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }

                            }
                        }
                    }
                }
            }

    }

    private fun showQuestions(i: Int) {
        listOfButtons = arrayListOf(buttonOne, buttonTwo, buttonThree, buttonFour)

        if (i == 0) {
            for (a in 0..3) {
                listOfButtons[a].visibility = View.VISIBLE
            }
        }

        val quizModel: QuizModel = quiz[i]
        questionText.text = quizModel.question
        listOfButtons.shuffle()

        listOfButtons[0].text = quizModel.answer
        listOfButtons[1].text = quizModel.option1
        listOfButtons[2].text = quizModel.option2
        listOfButtons[3].text = quizModel.option3
    }

    private fun saveToDB(i: Int) {
        var data: HashMap<String, Any>

        if (!solo) {
            db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            val shared =
                                activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val username = shared?.getString("username", "")
                            data = if (document.getString("player1") != username) {
                                hashMapOf("player2Score" to score, "player2Finished" to true)
                            } else {
                                hashMapOf("player1Score" to score, "player1Finished" to true)
                            }
                            db.collection("Games").document(gameID).collection("ActiveGames").document(
                                activeGameID
                            )
                                .set(data, SetOptions.merge()).addOnSuccessListener {
                                    if (i == 1) {
                                        val action =
                                            GameFragmentDirections.actionGameFragmentToResultFragment(
                                                gameID,
                                                activeGameID
                                            )
                                        findNavController().navigate(action)
                                    }
                                }
                        }
                    }
                }
        } else {
            data = hashMapOf("player1Score" to score)
            db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
                .set(data, SetOptions.merge()).addOnSuccessListener {
                    if (i == 1) {
                        val action =
                            GameFragmentDirections.actionGameFragmentToResultFragment(
                                gameID,
                                activeGameID
                            )
                        findNavController().navigate(action)
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()
        if (!gameFinished) {
            questionText.text = "Error"
            buttonOne.visibility = View.GONE
            buttonTwo.visibility = View.GONE
            buttonThree.visibility = View.GONE
            buttonFour.visibility = View.GONE
            saveToDB(0)
            score = 0
        }

    }
}