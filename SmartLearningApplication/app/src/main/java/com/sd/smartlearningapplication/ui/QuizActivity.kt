package com.sd.smartlearningapplication.ui

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import com.sd.smartlearningapplication.R
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.utìls.Resource
import com.sd.smartlearningapplication.viewModel.QuizViewModel
import kotlinx.android.synthetic.main.quiz_main_layout.*
import org.jetbrains.anko.design.snackbar


class QuizActivity : AppCompatActivity() {

    private var mCountDownTimer: CountDownTimer? = null
    private var mViewModel: QuizViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_main_layout)
        mViewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        mViewModel!!.init()
        observeViewModel();
    }

    private fun observeViewModel() {
        // Update the list when the data changes
        mViewModel?.mQuestionListObservable?.observe(this, Observer<Resource<List<QuestionModel>>> { resource ->
            loading_bar.visibility = View.GONE
            if (resource != null) {
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        llQuizComponent.visibility = View.VISIBLE
                        if (resource.data != null) {
                            mViewModel?.initQuestions(resource.data)
                            updateView()
                        }
                    }
                    Resource.Status.ERROR -> {
                        llQuizComponent.visibility = View.GONE
                        snackbar(rl_quiz_question, "Error:" + resource.exception)
                    }
                }
            }
        })
    }


    private fun updateView() {
        mViewModel!!.mTimer = 0
        initBtnBackground()
        //TODO:need tp update count when all questions are ready
        if (mViewModel!!.mTotalQuestions < 5) {
            val animate = TranslateAnimation(llQuizComponent.width + 0.0f, 0f, 0f,
                    0f)
            animate.duration = 250
            animate.fillAfter = true
            animate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    setCounter()
                    val questionModel: QuestionModel? = mViewModel!!.mQuestion
                    if (questionModel != null) {
                        questionTextView.text = questionModel.question
                        btnOption1.text = questionModel.choiceOne
                        btnOption2.text = questionModel.choiceTwo
                        btnOption3.text = questionModel.choiceThree
                        btnOption4.text = questionModel.choiceFour
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
            llQuizComponent.startAnimation(animate)
        } else {
            mViewModel!!.initializeDataTransferUtil()
            startActivity(Intent(this, QuizResultActivity::class.java))
            finish()
        }


    }

    private fun initBtnBackground() {
        btnOption1.background = getDrawable(R.drawable.quiz_button_selector)
        btnOption2.background = getDrawable(R.drawable.quiz_button_selector)
        btnOption3.background = getDrawable(R.drawable.quiz_button_selector)
        btnOption4.background = getDrawable(R.drawable.quiz_button_selector)
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
                }

            }
        }
        (mCountDownTimer as CountDownTimer).start()
    }

    private fun setTimerText() {
        timerText.text = "Time Remaining: " + (10 - mViewModel!!.mTimer) + "sec"
    }

    fun submitClick(view: View) {
        val correctButton: Button? = getCorrectAnsId()
        correctButton?.background = getDrawable(R.drawable.quiz_button_selector_right_answer)
        if (view.id == R.id.btnSkip) {
            updateViewModelAnswer("skip")
        } else {
            val ans: String = findViewById<Button>(view.id).text.toString()
            updateViewModelAnswer(ans)
            if (view.id != correctButton?.id) {
                view.background = getDrawable(R.drawable.quiz_button_selector_wrong_answer)
            }
        }
        mCountDownTimer!!.cancel()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        val mHandler = Handler()
        mHandler.postDelayed(Runnable {
            updateView()
        }, 3000L)
    }

    private fun getCorrectAnsId(): Button? {
        val ans: String? = mViewModel?.mQuestion?.correctAns
        when (ans) {
            btnOption1.text.toString() -> {
                return btnOption1
            }
            btnOption2.text.toString() -> {
                return btnOption2
            }
            btnOption3.text.toString() -> {
                return btnOption3
            }
            btnOption4.text.toString() -> {
                return btnOption4
            }
        }
        return null
    }

    private fun updateViewModelAnswer(answer: String?) {
        mViewModel!!.updateAnswer(answer)
    }
}
