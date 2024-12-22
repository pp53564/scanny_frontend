package com.scanny_project.data.model

data class UserQuestionAttemptDto (
        val id: Long?,
        val userId: Long,
        val questionId: Long,
        val attemptCount: Int,
        val succeeded: Boolean,
        val imagePath: String?
)