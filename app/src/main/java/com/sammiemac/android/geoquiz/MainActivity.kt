package com.sammiemac.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
//Chp. 3 Challenge: Preventing Repeat Answers
private const val KEY_REPEAT = "repeat"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    //Chp. 2 Challenge: Add a Previous Button
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
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
        cheatButton = findViewById(R.id.cheat_button)
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

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        //Chp. 2 Challenge: Add Listener to TextView
        questionTextView.setOnClickListener { view : View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
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

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
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
