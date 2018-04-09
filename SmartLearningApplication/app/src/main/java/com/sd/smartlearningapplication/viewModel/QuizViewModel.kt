package com.sd.smartlearningapplication.viewModel

import android.arch.lifecycle.ViewModel
import com.sd.smartlearningapplication.enums.TypeOfQuestion
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.model.QuestionResultModel
import com.sd.smartlearningapplication.utìls.DataTransferUtil

class QuizViewModel : ViewModel() {
    var mTimer: Int = 0
    private var mQuestionList: MutableList<QuestionModel> = ArrayList()
    var mQuestion: QuestionModel? = null
    var mTypeOfQuestion: TypeOfQuestion = TypeOfQuestion.EASY
    var mTotalQuestions: Int = 1
    var mQuestionResultModel: QuestionResultModel? = null

    fun init() {
        mQuestionList = getQuestionList()
        mQuestion = mQuestionList[0]
        mQuestion!!.isDone = true
        mQuestionResultModel = QuestionResultModel()
    }

    fun updateAnswer(answer: String?) {
        mTotalQuestions++
        //update the result model on basis of type of question asked
        updateResultModel()
        //Time is end then submit is pressed
        if (mTimer == 10) {
            mQuestion!!.correctlyAnswered = 2
            mQuestion = getNextQuestion()
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

    //Todo: Use live data to fetch questions and ans
    private fun getQuestionList(): MutableList<QuestionModel> {
        return object : java.util.ArrayList<QuestionModel>() {
            init {
                add(QuestionModel(question = "Who is current prime minister of India?", choiceOne = "Narendra Modi",
                        choiceTwo = "Manmohan Singh", choiceThree = "Pranav Mukherjee", choiceFour = "Ram Nath Kovind",
                        difficultyLevel = 1, correctAns = "Narendra Modi"))
                add(QuestionModel(question = "Who wrote National Anthem?", choiceOne = "Mohammad Iqbal",
                        choiceTwo = "Rabindra Nath Tagore", choiceThree = "Bankim Chandra Chatterjee", choiceFour = "Girija Kumar Mathur",
                        difficultyLevel = 1, correctAns = "Ram Nath Kovind"))
                add(QuestionModel(question = "Which of the following is India's capital?", choiceOne = "Mumbai",
                        choiceTwo = "Banglore", choiceThree = "Kolkata", choiceFour = "Delhi",
                        difficultyLevel = 1, correctAns = "Delhi"))
                add(QuestionModel(question = "Where is Taj Mahal situated?", choiceOne = "Agra",
                        choiceTwo = "Delhi", choiceThree = "Chennai", choiceFour = "Lucknow",
                        difficultyLevel = 1, correctAns = "Agra"))
                add(QuestionModel(question = "Who is current president of India?", choiceOne = "Narendra Modi",
                        choiceTwo = "Manmohan Singh", choiceThree = "Pranav Mukherjee", choiceFour = "Ram Nath Kovind",
                        difficultyLevel = 2, correctAns = "Ram Nath Kovind"))
                add(QuestionModel(question = "WHich city is known as \"City of Oranges\"?", choiceOne = "Nagpur",
                        choiceTwo = "Raipur", choiceThree = "Kanpur", choiceFour = "Jodhpur",
                        difficultyLevel = 2, correctAns = "Nagpur"))
                add(QuestionModel(question = "Who is 1st president of India?", choiceOne = "Narendra Modi",
                        choiceTwo = "Rajendra Prasad", choiceThree = "Pranav Mukherjee", choiceFour = "Jawahar Lal Nehru",
                        difficultyLevel = 2, correctAns = "Rajendra Prasad"))
                add(QuestionModel(question = "Which is India's National song?", choiceOne = "Jana Gana Mana",
                        choiceTwo = "Vande Mataram", choiceThree = "Saare Jaha se Accha", choiceFour = "Hum Honge Kamayab",
                        difficultyLevel = 3, correctAns = "Vande Mataram"))
                add(QuestionModel(question = "Which of the following is not a planet?", choiceOne = "Neptune",
                        choiceTwo = "Pluto", choiceThree = "Mars", choiceFour = "Venus",
                        difficultyLevel = 3, correctAns = "Pluto"))
            }
        }

    }
}