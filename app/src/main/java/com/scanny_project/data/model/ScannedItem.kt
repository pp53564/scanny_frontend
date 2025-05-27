package com.scanny_project.data.model


data class ScannedItem(
    val name: String,
    var isChecked: Boolean = true,
    var translations: MutableMap<String, String>
)
