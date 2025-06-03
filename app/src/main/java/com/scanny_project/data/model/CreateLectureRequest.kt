package com.scanny_project.data.model

data class CreateLectureRequest (
    val lectureName: String,
    val items: List<TranslatedItemDto>
)

