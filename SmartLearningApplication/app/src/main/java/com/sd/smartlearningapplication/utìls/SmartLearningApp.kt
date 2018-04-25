package com.sd.smartlearningapplication.utìls

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.sd.smartlearningapplication.database.QuestionDatabase

class SmartLearningApp : Application() {

    companion object {
        var database: QuestionDatabase? = null
        var mContext:Context?= null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        SmartLearningApp.database =  Room.databaseBuilder(this, QuestionDatabase::class.java,
                "question_db").build()
    }
}