package com.scanny_project.data.model

import android.graphics.RectF

data class CustomDetection(
    val boundingBox: RectF,
    val label: String,
    val score: Float
)
