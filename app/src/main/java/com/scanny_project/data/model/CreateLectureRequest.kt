package com.scanny_project.data.model

data class CreateLectureRequest (
    val lectureName: String,
    val items: List<TranslatedItemDto>
)

data class TranslatedItemDto(
    val base: String,
    val translations: Map<String, String>
)
