package com.sd.smartlearningapplication.utìls

import com.sd.smartlearningapplication.model.QuestionModel
import com.sd.smartlearningapplication.model.QuestionResultModel

//Todo find better ways to transfer large data  between activities
object DataTransferUtil {
    var mQuestionList: MutableList<QuestionModel> = ArrayList()
    var mQuestionResultModel: QuestionResultModel? = null
}