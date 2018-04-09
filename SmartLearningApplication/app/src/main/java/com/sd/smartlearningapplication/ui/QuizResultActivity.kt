package com.sd.smartlearningapplication.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.sd.smartlearningapplication.R
import com.sd.smartlearningapplication.utìls.DataTransferUtil
import kotlinx.android.synthetic.main.activity_quiz_result.*

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        init()
    }

    private fun init() {
        tvTotal.text= getString(R.string.total_question)
        tvRightAnswer.text=DataTransferUtil.mQuestionResultModel!!.rightAnswer.toString()
        tvWrongAnswer.text=DataTransferUtil.mQuestionResultModel!!.wrongAns.toString()
    }
}
