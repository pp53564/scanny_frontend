package com.scanny_project.data

import android.graphics.Bitmap
import android.util.Log
import com.scanny_project.data.model.AttemptResponse
import com.scanny_project.data.services.UserQuestionAttemptService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttemptsRepository @Inject constructor(
    private val attemptsApi: UserQuestionAttemptService
) {
    suspend fun recordAttempt(
        userId: Long,
        questionId: Long,
        imageBitmap: Bitmap?
    ): Result<AttemptResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val userIdBody =
                    userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val questionIdBody =
                    questionId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val file = File.createTempFile("attempt_image", ".jpg")
                    val outputStream = FileOutputStream(file)
                imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                    var imagePart =
                        MultipartBody.Part.createFormData("correctImage", file.name, requestFile)

                val response = attemptsApi.recordAttempt(
                    userId = userIdBody,
                    questionId = questionIdBody,
                    correctImage = imagePart
                )

                if (response.isSuccessful) {
                    Log.i("AttemptsRepository", "recordAttempt: Success - ${response.body()}")
                    Result.Success(response.body()!!)
                } else {
                    Log.e("AttemptsRepository", "recordAttempt: Failed - ${response.code()}, ${response.message()}")
                    Result.Error(Exception("Error recording attempt: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("AttemptsRepository", "recordAttempt: Exception - ${e.message}", e)
                Result.Error(e)
            }
        }
    }
}