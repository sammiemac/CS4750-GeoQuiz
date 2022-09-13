package com.sammiemac.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    //Chp. 3 Challenge: Preventing Repeat Answers
    val currentQuestionRepeat: Boolean
        get() = questionBank[currentIndex].repeat

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    //Chp. 2 Challenge: Add a Previous Button
    fun moveToPrev() {
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        } else {
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }

    //Chp. 3 Challenge: Preventing Repeat Answers
    fun checkRepeat() {
        questionBank[currentIndex].repeat = true
    }

    //Chp. 3 Challenge: Preventing Repeat Answers
    fun setRepeat(repeat: Boolean) {
        questionBank[currentIndex].repeat = repeat
    }
}