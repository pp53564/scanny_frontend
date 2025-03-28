package com.scanny_project.data.model

data class UserQuestionDTO(
    val id: Long,
    val subject: String,
    val localizedSubject: String,
    val attemptCount: Int,
    val succeeded: Boolean
)
