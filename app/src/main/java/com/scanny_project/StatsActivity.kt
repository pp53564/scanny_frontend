package com.scanny_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui_ux_demo.R

data class LanguageStats(
    val languageName: String,
    val correctAnswers: Int,
    val totalQuestions: Int
) {
    val accuracy: Double
        get() = if (totalQuestions == 0) 0.0 else correctAnswers * 100.0 / totalQuestions
}

val quizStats = listOf(
    LanguageStats("English", 45, 50),
    LanguageStats("French", 30, 50),
    LanguageStats("German", 20, 50),
    LanguageStats("Italian", 35, 50),
    LanguageStats("Spanish", 50, 50)
)

class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
    }
}