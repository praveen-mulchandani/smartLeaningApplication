package com.sd.smartlearningapplication.model

import android.os.Parcel
import android.os.Parcelable

data class QuestionResultModel(var easyAverageTime: Float = 10f, var mediumAverageTime: Float = 10f,
                               var hardAverageTime: Float = 10f,
                               var easyQuestionsAnswered: Int = 0, var mediumQuestionsAnswered: Int = 0,
                               var hardQuestionsAnswered: Int = 0,
                               var rightAnswer: Int = 0, var wrongAns: Int = 0)