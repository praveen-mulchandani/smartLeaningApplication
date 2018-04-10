package com.sd.smartlearningapplication.model

data class QuestionResultModel(var easyAverageTime: Float = 10f, var mediumAverageTime: Float = 10f,
                               var hardAverageTime: Float = 10f,
                               var easyQuestionsAnswered: Int = 0, var mediumQuestionsAnswered: Int = 0,
                               var hardQuestionsAnswered: Int = 0,
                               var rightAnswer: Int = 0, var wrongAns: Int = 0, var skippedAns: Int = 0)