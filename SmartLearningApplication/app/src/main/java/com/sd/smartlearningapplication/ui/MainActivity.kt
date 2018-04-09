package com.sd.smartlearningapplication.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sd.smartlearningapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startQuizClickHandler(view: View) {
        startActivity(Intent(this, QuizActivity::class.java))
    }
}
