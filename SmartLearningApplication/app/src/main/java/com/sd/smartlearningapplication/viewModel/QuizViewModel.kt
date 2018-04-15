package com.sd.smartlearningapplication.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.sd.smartlearningapplication.enums.TypeOfQuestion
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.model.QuestionResultModel
import com.sd.smartlearningapplication.network.QuestionRepository
import com.sd.smartlearningapplication.utìls.DataTransferUtil
import com.sd.smartlearningapplication.utìls.Resource


class QuizViewModel : ViewModel() {
    var mTimer: Int = 0
    private var mQuestionList: MutableList<QuestionModel> = ArrayList()
    var mQuestion: QuestionModel? = null
    var mTypeOfQuestion: TypeOfQuestion = TypeOfQuestion.EASY
    var mTotalQuestions: Int = 1
    var mQuestionResultModel: QuestionResultModel? = null
    var mQuestionListObservable: LiveData<Resource<List<QuestionModel>>>? = null

    fun init() {
        mQuestionListObservable = QuestionRepository.getQuestionsList()
    }

    fun initQuestions(questionList: List<QuestionModel>) {
        mQuestionList = questionList.toMutableList()
        mQuestion = mQuestionList[0]
        mQuestion!!.isDone = true
        mQuestionResultModel = QuestionResultModel()
    }

    fun updateAnswer(answer: String?) {
        mTotalQuestions++
        //update the result model on basis of type of question asked
        updateResultModel()
        //Time is end then submit is pressed
        if (answer.equals("skip")) {
            mQuestion!!.correctlyAnswered = 2
            mQuestion = getNextQuestion()
            mQuestionResultModel!!.skippedAns++
        } else if (mQuestion?.correctAns.equals(answer)) {
            mQuestion!!.correctlyAnswered = 1
            mQuestionResultModel!!.rightAnswer++
            when (mTypeOfQuestion) {
                TypeOfQuestion.EASY -> {
                    mTypeOfQuestion = TypeOfQuestion.MEDIUM
                }
                TypeOfQuestion.MEDIUM -> {
                    mTypeOfQuestion = TypeOfQuestion.HARD
                }
                TypeOfQuestion.HARD -> {
                    mTypeOfQuestion = TypeOfQuestion.HARD
                }
            }
            mQuestion = getNextQuestion()

        } else {
            mQuestionResultModel!!.wrongAns++
            mQuestion!!.correctlyAnswered = 2
            mQuestion = getNextQuestion()
        }

    }

    private fun updateResultModel() {
        when (mTypeOfQuestion) {
            TypeOfQuestion.EASY -> {
                mQuestionResultModel!!.easyQuestionsAnswered++
                mQuestionResultModel!!.easyAverageTime += mTimer
            }
            TypeOfQuestion.MEDIUM -> {
                mQuestionResultModel!!.mediumQuestionsAnswered++
                mQuestionResultModel!!.mediumAverageTime += mTimer
            }
            TypeOfQuestion.HARD -> {
                mQuestionResultModel!!.hardQuestionsAnswered++
                mQuestionResultModel!!.hardAverageTime += mTimer
            }
        }
    }

    private fun getNextQuestion(): QuestionModel? {
        for (questionModel in mQuestionList)
            if (mTypeOfQuestion == TypeOfQuestion.getTypeOfQuestion(questionModel.difficultyLevel) &&
                    !questionModel.isDone) {
                questionModel.isDone = true
                return questionModel
            }
        return null
    }

    fun initializeDataTransferUtil() {
        DataTransferUtil.mQuestionList = mQuestionList
        DataTransferUtil.mQuestionResultModel = mQuestionResultModel
    }

}