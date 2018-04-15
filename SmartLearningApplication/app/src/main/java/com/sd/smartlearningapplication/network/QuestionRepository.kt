package com.sd.smartlearningapplication.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.utìls.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object QuestionRepository {
    fun getQuestionsList(): LiveData<Resource<List<QuestionModel>>> {
        val data = MutableLiveData<Resource<List<QuestionModel>>>();
        val apiService = RetrofitClient.retrofit.create(APIService::class.java)
        apiService.getQuestions().enqueue(object : Callback<List<QuestionModel>> {
            override fun onResponse(call: Call<List<QuestionModel>>?, response: Response<List<QuestionModel>>?) {
                data.value = Resource.success(response?.body());
            }

            override fun onFailure(call: Call<List<QuestionModel>>?, t: Throwable?) {
                data.value = Resource.error(t!!.message)
            }
        });
        return data;
    }
}
