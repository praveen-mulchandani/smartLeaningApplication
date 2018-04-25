package com.sd.smartlearningapplication.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.sd.smartlearningapplication.entities.QuestionEntity

@Dao
interface QuestionsDao {
    @Query("SELECT * from questions")
    fun getAll(): List<QuestionEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(questions: QuestionEntity)

    @Query("DELETE from questions")
    fun deleteAll()
}
