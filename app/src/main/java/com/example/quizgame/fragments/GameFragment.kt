package com.example.quizgame.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.quizgame.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GameFragment : Fragment(R.layout.fragment_game) {
    private val db = Firebase.firestore
    private var trueList = ArrayList<Long>()
    private lateinit var questionText:TextView
    private lateinit var buttonOne:Button
    private lateinit var buttonTwo:Button
    private lateinit var buttonThree:Button
    private lateinit var buttonFour:Button

    // get quiz from db
    fun getQuizQuestions() {
        val gameID = "yTqC73YLuhbt1buiK5eo"
        val activeGameID = "nFnnTJoP9Mn2kUD8xOWp"

        db.collection("Games").document(gameID).collection("ActiveGames").document(activeGameID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {

                        trueList = (document["questions"] as ArrayList<Long>?)!!
                        //Do what you need to do with your ArrayList
                        swagList(trueList, gameID, activeGameID)
                    }
                }
            }
    }

    //print to buttons

    private fun swagList(trueList: ArrayList<Long>, gameID: String, activeGameID: String) {

        for(i in 0..1) {

            db.collection("Games").document(gameID).collection("Quiz")
                .document(trueList[i].toString())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document!!.exists()) {
                            questionText.text = (document["question"]).toString()
                            buttonOne.text = (document["answer"]).toString()
                            buttonTwo.text = (document["option1"]).toString()
                            buttonThree.text = (document["option2"]).toString()
                            buttonFour.text = (document["option3"]).toString()

                            //timer

                        }
                    }
                }





        }//end of loop

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         questionText = view.findViewById(R.id.questionTextView)
         buttonOne = view.findViewById(R.id.buttonOne)
         buttonTwo = view.findViewById(R.id.buttonTwo)
         buttonThree = view.findViewById(R.id.buttonThree)
         buttonFour = view.findViewById(R.id.buttonFour)

        val score = 0
        getQuizQuestions()

        val gameID = "yTqC73YLuhbt1buiK5eo"
        val activeGameID = "nFnnTJoP9Mn2kUD8xOWp"


        val countdownText = view.findViewById<TextView>(R.id.countDownTextView)

        // method call to timer??
        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdownText.text = millisUntilFinished.toString()
            }

            override fun onFinish() {
                countdownText.text="0"
                //next question
                //lock buttons
                //update text for questions/buttons
                //unlock buttons
            }
        }
        timer.start()

        // if button answer == clicked --> score++;

        //index++

        // end of loop


    }


    // save score etc to db
    fun saveToDB() {



    }
}