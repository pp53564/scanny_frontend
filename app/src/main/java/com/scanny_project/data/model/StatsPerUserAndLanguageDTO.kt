package com.scanny_project.data.model

data class StatsPerUserAndLanguageDTO(
    val languageCode: String,
    val correctAnswers: Long,
    val attemptSum: Long,
    val score: Long,
    val rank: Integer,
    val totalUsersInLang: Integer,
    val totalQuestions: Long
)
