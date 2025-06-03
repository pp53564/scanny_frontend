package com.scanny_project.data.model

data class TranslatedItemDto(
    val base: String,
    val translations: Map<String, String>
)