package com.scanny_project.data.api

import com.scanny_project.data.model.AttemptResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
interface UserQuestionAttemptApi {
    @Multipart
    @POST("api/attempts/attempt")
    suspend fun recordAttempt(
        @Part("questionId") questionId: RequestBody,
        @Part correctImage: MultipartBody.Part,
        @Part("langCode") langCode: RequestBody
    ): Response<AttemptResponse>
}