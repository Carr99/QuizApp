package com.example.quizgame.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quizgame.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GameFragment : Fragment(R.layout.fragment_game) {
    private val db = Firebase.firestore
    private var trueList = ArrayList<Long>()
    private lateinit var questionText: TextView
    private lateinit var buttonOne: Button
    private lateinit var buttonTwo: Button
    private lateinit var buttonThree: Button
    private lateinit var buttonFour: Button
    private lateinit var countdownText: TextView
    private val args: ResultFragmentArgs by navArgs()
    private var score = 0

    // get quiz from db
    fun getQuizQuestions(listOfButtons: ArrayList<Button>) {
        val gameID = args.gameID
        val activeGameID = args.activeGameID

        db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {

                        trueList = (document["questions"] as ArrayList<Long>?)!!
                        //Do what you need to do with your ArrayList
                        printQuestions(listOfButtons,0)
                    }
                }
            }
    }

    //print to buttons

    // make 5 arrays, one for each text / button, add all of the same, in timer finish print next index to buttons
    private fun printQuestions(
        listOfButtons: ArrayList<Button>,
        counter : Int
    ) {
            listOfButtons.shuffle()
            db.collection("Games").document(args.gameID).collection("Quiz")
                .document(trueList[counter].toString())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            questionText.text = (document["question"]).toString()
                            listOfButtons[0].text = (document["answer"]).toString()
                            listOfButtons[1].text = (document["option1"]).toString()
                            listOfButtons[2].text = (document["option2"]).toString()
                            listOfButtons[3].text = (document["option3"]).toString()

                        }
                    }
                }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionText = view.findViewById(R.id.questionTextView)
        buttonOne = view.findViewById(R.id.buttonOne)
        buttonTwo = view.findViewById(R.id.buttonTwo)
        buttonThree = view.findViewById(R.id.buttonThree)
        buttonFour = view.findViewById(R.id.buttonFour)
        countdownText = view.findViewById(R.id.countDownTextView)
        val listOfButtons: ArrayList<Button> =
            arrayListOf(buttonOne, buttonTwo, buttonThree, buttonFour)

        buttonOne.setOnClickListener {
            if (listOfButtons[0] == buttonOne) {
                score++
                // next question
                Log.e("Button 1 score", score.toString())
            }
        }
        buttonTwo.setOnClickListener {
            if (listOfButtons[0] == buttonTwo) {
                score++
                // next question
                Log.e("Button 2 score", score.toString())
            }
        }
        buttonThree.setOnClickListener {
            if (listOfButtons[0] == buttonThree) {
                score++
                // next question
                Log.e("Button 3 score", score.toString())
            }
        }
        buttonFour.setOnClickListener {
            if (listOfButtons[0] == buttonFour) {
                score++
                // next question
                Log.e("Button 4 score", score.toString())
            }
        }

        getQuizQuestions(listOfButtons)

        for (i in 1..3) {
            val timer = object : CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countdownText.text = millisUntilFinished.toString()
                }

                override fun onFinish() {
                    printQuestions(listOfButtons, i)
                }
            }
            timer.start()

            Log.e("i",i.toString())


        }
    }


    // save score etc to db
    fun saveToDB() {


    }
}