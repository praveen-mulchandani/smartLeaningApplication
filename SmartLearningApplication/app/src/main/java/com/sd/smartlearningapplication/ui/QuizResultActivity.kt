package com.sd.smartlearningapplication.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sd.smartlearningapplication.R
import com.sd.smartlearningapplication.classifier.LevelPredictorClassifier
import com.sd.smartlearningapplication.model.QuestionResultModel
import com.sd.smartlearningapplication.utìls.DataTransferUtil
import kotlinx.android.synthetic.main.activity_quiz_result.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class QuizResultActivity : AppCompatActivity() {
    private val MODEL_FILE = "file:///android_asset/quiz_graph.pb"
    private val INPUT_NAME = "input"
    private val OUTPUT_NAME = "output"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        initTensorFlowAndLoadModel()
        init()
    }

    private fun init() {
        tvTotal.text = getString(R.string.total_question)
        tvRightAnswer.text = "Correct - " + DataTransferUtil.mQuestionResultModel!!.rightAnswer.toString()
        tvWrongAnswer.text = "Wrong - " + DataTransferUtil.mQuestionResultModel!!.wrongAns.toString()
        tvSkippedAnswer.text = "Skipped - " + DataTransferUtil.mQuestionResultModel!!.skippedAns.toString()
    }

    private fun initTensorFlowAndLoadModel() {
        val questionResultModel: QuestionResultModel = DataTransferUtil.mQuestionResultModel!!
        questionResultModel.hardAverageTime = calculateAverageTime(questionResultModel.hardAverageTime,
                questionResultModel.hardQuestionsAnswered)

        questionResultModel.easyAverageTime = calculateAverageTime(questionResultModel.easyAverageTime,
                questionResultModel.easyQuestionsAnswered)

        questionResultModel.mediumAverageTime = calculateAverageTime(questionResultModel.mediumAverageTime,
                questionResultModel.mediumQuestionsAnswered)
        val inputArray = floatArrayOf(questionResultModel.hardQuestionsAnswered.toFloat(),
                questionResultModel.easyQuestionsAnswered.toFloat(),
                questionResultModel.mediumQuestionsAnswered.toFloat(), questionResultModel.hardAverageTime,
                questionResultModel.easyAverageTime, questionResultModel.mediumAverageTime)

        async(UI) {
            try {
                val testClassifier = LevelPredictorClassifier()
                val floats = bg {
                    testClassifier.create(
                            assets,
                            MODEL_FILE,
                            INPUT_NAME,
                            OUTPUT_NAME, inputArray)
                }
                tvPredictedLevel.text = "Predicted Level - " + getPredictedLevel(floats.await()[0])
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    fun retryClick(view: View) {
        finish()
        startActivity(Intent(this, QuizActivity::class.java))
    }

    private fun getPredictedLevel(score: Float): String {
        val scoreLevel = score.toInt()
        when (scoreLevel) {
            0 -> {
                return "Genius"
            }
            1 -> {
                return "Genius"
            }
            2 -> {
                return "Intelligent"
            }
            3 -> {
                return "Smart"
            }
            4 -> {
                return "Good effort"
            }
            5 -> {
                return "Need to work hard"
            }
            else -> {
                return "Need to work hard"
            }
        }

    }

    private fun calculateAverageTime(averageTime: Float, questionsAnswered: Int): Float {
        var avgTime = averageTime / questionsAnswered
        if (avgTime.isInfinite()) {
            avgTime = 10f
        }
        return avgTime
    }
}
