package com.scanny_project.utils

import com.example.ui_ux_demo.R

data class LanguageOption(
    val code: String,
    val name: String,
    val flagResId: Int
)

object LanguageData {
    val languages = listOf(
        LanguageOption("hr", "Hrvatski", R.drawable.flag_hr),
        LanguageOption("en", "Engleski", R.drawable.flag_en),
        LanguageOption("it", "Talijanski", R.drawable.flag_it),
        LanguageOption("de", "Njemački", R.drawable.flag_de),
        LanguageOption("fr", "Francuski", R.drawable.flag_fr)
    )
}

