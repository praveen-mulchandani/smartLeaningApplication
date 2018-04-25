package com.sd.smartlearningapplication.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sd.smartlearningapplication.dao.QuestionsDao
import com.sd.smartlearningapplication.entities.QuestionEntity

@Database(entities = [(QuestionEntity::class)], version = 1)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionsDao
}