package com.scanny_project.data.model

data class LectureDTO(
    val id: Long,
    val title: String,
    val questions: List<QuestionDTO>
)
