package com.sd.smartlearningapplication.network

import com.sd.smartlearningapplication.model.QuestionModel
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    /**
     * Gets question model.
     *
     * @return the question model data
     */
    @GET("/bins/19fazb")
    fun getQuestions(): Call<List<QuestionModel>>
}