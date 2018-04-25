package com.sd.smartlearningapplication.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
        @PrimaryKey(autoGenerate = true)
        val uid: Long =0,
        var question: String, var choiceOne: String,
        var choiceTwo: String, var choiceThree: String,
        var choiceFour: String, var difficultyLevel: Int,
        var correctAns: String)