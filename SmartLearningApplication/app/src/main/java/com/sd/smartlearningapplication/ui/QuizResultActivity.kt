package com.sd.smartlearningapplication.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sd.smartlearningapplication.R
import com.sd.smartlearningapplication.classifier.LevelPredictorClassifier
import com.sd.smartlearningapplication.model.QuestionResultModel
import com.sd.smartlearningapplication.utìls.DataTransferUtil
import kotlinx.android.synthetic.main.activity_quiz_result.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.concurrent.Executors

class QuizResultActivity : AppCompatActivity() {
    private val MODEL_FILE = "file:///android_asset/quiz_graph.pb"
    private val INPUT_NAME = "input"
    private val OUTPUT_NAME = "output"
    private val executor = Executors.newSingleThreadExecutor()
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
        tvSkippedAnswer.text= "Skipped - " + DataTransferUtil.mQuestionResultModel!!.skippedAns.toString()
    }

    private fun initTensorFlowAndLoadModel() {
        val questionResultModel: QuestionResultModel = DataTransferUtil.mQuestionResultModel!!
        val inputArray = floatArrayOf(questionResultModel.hardQuestionsAnswered.toFloat(),
                questionResultModel.easyQuestionsAnswered.toFloat(),
                questionResultModel.mediumQuestionsAnswered.toFloat(),
                (questionResultModel.hardAverageTime / questionResultModel.hardQuestionsAnswered),
                (questionResultModel.easyAverageTime / questionResultModel.easyQuestionsAnswered),
                (questionResultModel.mediumAverageTime / questionResultModel.mediumQuestionsAnswered)
        )

        async(UI){
                try {
                    val testClassifier = LevelPredictorClassifier()
                    val floats = bg {
                        testClassifier.create(
                                assets,
                                MODEL_FILE,
                                INPUT_NAME,
                                OUTPUT_NAME, inputArray)
                    }
                    tvPredictedLevel.text = "Predicted Level - " + floats.await()[0]
                } catch (e: Exception) {
                    throw RuntimeException("Error initializing TensorFlow!", e)
                }
        }


        executor.execute {

        }
    }
}
