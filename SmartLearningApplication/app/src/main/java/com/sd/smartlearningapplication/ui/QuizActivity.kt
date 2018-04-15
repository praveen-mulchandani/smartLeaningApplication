package com.sd.smartlearningapplication.ui

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
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
import com.sd.smartlearningapplication.utìls.Resource
import com.sd.smartlearningapplication.viewModel.QuizViewModel
import kotlinx.android.synthetic.main.question_header.*
import kotlinx.android.synthetic.main.quiz_component.*
import kotlinx.android.synthetic.main.single_choice_question.*
import org.jetbrains.anko.design.snackbar


class QuizActivity : AppCompatActivity() {

    private var mCountDownTimer: CountDownTimer? = null
    private var mViewModel: QuizViewModel? = null
    private var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_component)
        mViewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        progress = ProgressDialog(this, 0)
        progress!!.setTitle("Loading")
        progress!!.setMessage("Wait while loading...")
        progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress!!.show()
        mViewModel!!.init()
        observeViewModel();
    }

    private fun observeViewModel() {
        // Update the list when the data changes
        mViewModel?.mQuestionListObservable?.observe(this, Observer<Resource<List<QuestionModel>>> { resource ->
            progress!!.hide()
            if (resource != null) {
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        if (resource.data != null) {
                            mViewModel?.initQuestions(resource.data)
                            setCounter()
                            updateView()
                        }
                    }
                    Resource.Status.ERROR -> {
                        snackbar(llQuizComponent, "Error:" + resource.exception)
                    }
                }
            }
        })
    }


    private fun updateView() {
        //TODO:need tp update count when all questions are ready
        if (mViewModel!!.mTotalQuestions < 5) {
            val animate = TranslateAnimation(llQuizComponent.width + 0.0f, 0f, 0f, 0f)
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
                }

            }
        }
        (mCountDownTimer as CountDownTimer).start()
    }

    private fun setTimerText() {
        timerText.text = "Time Remaining: " + (10 - mViewModel!!.mTimer) + "sec"
    }

    fun submitClick(view: View) {
        if (view.id == R.id.btnSubmit) {
            val checkedId: Int = radio_group.checkedRadioButtonId
            if (checkedId != -1) {
                val ans: String = findViewById<RadioButton>(checkedId).text.toString()
                radio_group.clearCheck()
                updateViewModelAnswer(ans)
                updateView()
                mViewModel!!.mTimer = 0
                setCounter()
            } else {
                snackbar(llQuizComponent, "Please check an answer")
            }
        } else {
            radio_group.clearCheck()
            updateViewModelAnswer("skip")
            updateView()
            mViewModel!!.mTimer = 0
            setCounter()
        }

    }

    private fun updateViewModelAnswer(answer: String?) {
        mViewModel!!.updateAnswer(answer)
    }
}
