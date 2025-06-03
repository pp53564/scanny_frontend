package com.scanny_project.data.repository

import android.graphics.Bitmap
import com.scanny_project.data.model.AttemptResponse
import com.scanny_project.utils.Result

interface AttemptsRepository {
    suspend fun recordAttempt(
        questionId: Long,
        imageBitmap: Bitmap?,
        langCode: String,
    ): Result<AttemptResponse>
}