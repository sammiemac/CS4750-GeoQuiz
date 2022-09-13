package com.sammiemac.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
//Chp. 3 Challenge: Preventing Repeat Answers
private const val KEY_REPEAT = "repeat"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    //Chp. 2 Challenge: Add a Previous Button
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        //Chp. 3 Challenge: Preventing Repeat Answers
        val repeat = savedInstanceState?.getBoolean(KEY_REPEAT, false) ?: false
        quizViewModel.setRepeat(repeat)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_image_button)
        //Chp. 2 Challenge: Add a Previous Button
        prevButton = findViewById(R.id.prev_image_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            //Chp. 3 Challenge: Preventing Repeat Answers
            if (!quizViewModel.currentQuestionRepeat) {
                checkAnswer(true)
            } else {
                alreadyAnswered()
            }

        }

        falseButton.setOnClickListener { view: View ->
            //Chp. 3 Challenge: Preventing Repeat Answers
            if (!quizViewModel.currentQuestionRepeat) {
                checkAnswer(false)
            } else {
                alreadyAnswered()
            }
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        //Chp. 2 Challenge: Add a Previous Button
        prevButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        //Chp. 2 Challenge: Add Listener to TextView
        questionTextView.setOnClickListener { view : View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        //Chp. 3 Challenge: Preventing Repeat Answers
        savedInstanceState.putBoolean(KEY_REPEAT, quizViewModel.currentQuestionRepeat)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        //Chp. 3 Challenge: Preventing Repeat Answers
        quizViewModel.checkRepeat()

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    //Chp. 3 Challenge: Preventing Repeat Answers
    private fun alreadyAnswered() {
        val messageResId = R.string.already_answered_toast
        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT)
            .show()
    }

}
