package com.scanny_project.data.model

data class StatsPerUserAndLanguageDTO(
    val language: String,
    val totalQuestions: Long,
    val answered: Long,
    val correct: Long,
    val accuracy: Double,
    val avgAttemptsPerQuestion: Double,
    val avgAttemptsForCorrect: Double
)
