package com.sd.smartlearningapplication.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.RadioButton
import com.sd.smartlearningapplication.R
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.viewModel.QuizViewModel
import kotlinx.android.synthetic.main.question_header.*
import kotlinx.android.synthetic.main.quiz_component.*
import kotlinx.android.synthetic.main.single_choice_question.*


class QuizActivity : AppCompatActivity() {

    private var mCountDownTimer: CountDownTimer? = null
    private var mViewModel: QuizViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_component)
        mViewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        mViewModel!!.init()
        setCounter()
        updateView()
    }

    private fun updateView() {
        //TODO:need tp update count when all questions are ready
        if (mViewModel!!.mTotalQuestions < 5) {
            val animate = TranslateAnimation(llQuizComponent.width +0.0f, 0f, 0f, 0f)
            animate.duration = 250
            animate.fillAfter = true
            llQuizComponent.startAnimation(animate)

            val questionModel: QuestionModel? = mViewModel!!.mQuestion
            if (questionModel != null) {
                questionTextView.text = questionModel.question
                radioButton1.text = questionModel.choiceOne
                radioButton2.text = questionModel.choiceTwo
                radioButton3.text = questionModel.choiceThree
                radioButton4.text = questionModel.choiceFour
            }
        } else {
            mViewModel!!.initializeDataTransferUtil()
            startActivity(Intent(this, QuizResultActivity::class.java))
            finish()
        }


    }

    private fun setCounter() {

        progressbar.progress = mViewModel!!.mTimer
        progressbar.progressDrawable = getDrawable(R.drawable.progress_bar_drawable)
        mCountDownTimer = object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                setTimerText()
                if (mViewModel!!.mTimer < 10) {
                    mViewModel!!.mTimer++
                    progressbar.progress = mViewModel!!.mTimer * 100 / (10000 / 1000)
                }
                if (mViewModel!!.mTimer > 3) {
                    progressbar.progressDrawable = getDrawable(R.drawable.progress_bar_drawable_intermediate)
                }
                if (mViewModel!!.mTimer > 7) {
                    progressbar.progressDrawable = getDrawable(R.drawable.progress_bar_drawable_end)
                }


            }

            override fun onFinish() {
                if (mViewModel!!.mTimer < 10) {
                    setTimerText()
                    mViewModel!!.mTimer++
                    progressbar.progress = 100
                    setTimerText()
                    updateView()
                }

            }
        }
        (mCountDownTimer as CountDownTimer).start()
    }

    private fun setTimerText() {
        timerText.text = "Time Remaining: " + (10 - mViewModel!!.mTimer) + "sec"
    }

    fun submitClick(view: View) {
        val checkedId: Int = radio_group.checkedRadioButtonId
        val ans: String = findViewById<RadioButton>(checkedId).text.toString()
        radio_group.clearCheck()
        updateViewModelAnswer(ans)
        updateView()
        mViewModel!!.mTimer = 0
        setCounter()

    }

    private fun updateViewModelAnswer(answer: String?) {
        mViewModel!!.updateAnswer(answer)
    }
}
