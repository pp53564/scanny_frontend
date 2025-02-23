package com.scanny_project.data.model

data class AttemptResponse(
     val correct: Boolean,
     val confidenceScore: Float,
     val message: String,
     val matchedLabel: String
)
