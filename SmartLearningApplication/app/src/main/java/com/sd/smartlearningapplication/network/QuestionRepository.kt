package com.sd.smartlearningapplication.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sd.smartlearningapplication.entities.QuestionEntity
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.utìls.NetworkUtil
import com.sd.smartlearningapplication.utìls.Resource
import com.sd.smartlearningapplication.utìls.SmartLearningApp
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object QuestionRepository {
    fun getQuestionsList(): LiveData<Resource<List<QuestionModel>>> {
        val data = MutableLiveData<Resource<List<QuestionModel>>>();
        if (NetworkUtil.checkNetwork(SmartLearningApp.mContext!!)) {
            val apiService = RetrofitClient.retrofit.create(APIService::class.java)
            apiService.getQuestions().enqueue(object : Callback<List<QuestionModel>> {
                override fun onResponse(call: Call<List<QuestionModel>>?, response: Response<List<QuestionModel>>?) {
                    data.value = Resource.success(getRandomQuestions(response?.body()));
                    async(UI) {
                        bg {
                            insertIntoDatabase(response?.body())
                        }
                    }
                }

                override fun onFailure(call: Call<List<QuestionModel>>?, t: Throwable?) {
                    data.value = Resource.error(t!!.message)
                }
            })
        } else {
            async(UI) {
                data.value = bg {
                    getQuestionFromDatabase()
                }.await()
            }

        }
        return data;
    }

    private fun getRandomQuestions(questionsList: List<QuestionModel>?): List<QuestionModel>? {
        val randomQuestionList:MutableList<QuestionModel>? = mutableListOf();
        if (questionsList != null) {
            val easyQuestionList = questionsList.filter { it.difficultyLevel == 1 }.shuffled()
            val mediumQuestionList = questionsList.filter { it.difficultyLevel == 2 }.shuffled()
            val hardQuestionsList = questionsList.filter { it.difficultyLevel == 3 }.shuffled()
            randomQuestionList!!.addAll(easyQuestionList)
            randomQuestionList.addAll(mediumQuestionList)
            randomQuestionList.addAll(hardQuestionsList)
        }
        return randomQuestionList
    }

    private fun getQuestionFromDatabase(): Resource<List<QuestionModel>> {
        val questionEntity: List<QuestionEntity>? = SmartLearningApp.database?.questionDao()?.getAll()
        return if (questionEntity == null || questionEntity.isEmpty()) {
            Resource.error("No question found. Please connect to internet!!")
        } else {
            val questionsModel = mutableListOf<QuestionModel>();
            for (questions in questionEntity) {
                questionsModel.add(QuestionModel(question = questions.question, choiceFour = questions.choiceFour,
                        choiceThree = questions.choiceThree, choiceTwo = questions.choiceTwo,
                        choiceOne = questions.choiceOne, difficultyLevel = questions.difficultyLevel,
                        correctAns = questions.correctAns))
            }
            return Resource.success(getRandomQuestions(questionsModel))
        }
    }

    private fun insertIntoDatabase(questionModel: List<QuestionModel>?) {
        if (questionModel != null) {
            val questionList: Resource<List<QuestionModel>> = getQuestionFromDatabase()
            if (questionList.data != null && questionList.data.isNotEmpty()) {
                SmartLearningApp.database?.questionDao()?.deleteAll()
            }
            for (questions in questionModel) {
                val questionEntity: QuestionEntity = QuestionEntity(question = questions.question,
                        correctAns = questions.correctAns, difficultyLevel = questions.difficultyLevel,
                        choiceOne = questions.choiceOne, choiceTwo = questions.choiceTwo,
                        choiceThree = questions.choiceThree, choiceFour = questions.choiceFour)
                SmartLearningApp.database?.questionDao()?.insertAll(questionEntity)
            }
        }

    }
}
