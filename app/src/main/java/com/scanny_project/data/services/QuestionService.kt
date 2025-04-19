package com.scanny_project.data.services

import com.scanny_project.data.model.AnsweredQuestionDTO
import com.scanny_project.data.model.QuestionDTO
import com.scanny_project.data.model.UserQuestionDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestionService {
    @GET("api/questions/lecture/{lectureId}")
    suspend fun getQuestionsByLecture(@Path("lectureId") lectureId: Long): Response<List<QuestionDTO>>

    @GET("api/questions/lecture/{lectureId}/user")
    suspend fun getUserQuestionsByLecture(@Path("lectureId") lectureId: Long): Response<List<UserQuestionDTO>>
    @GET("api/questions/lecture/{lectureId}/user/{langCode}")
    suspend fun getUserQuestionsByLectureAndLang(@Path("lectureId") lectureId: Long, @Path("langCode") langCode: String): Response<List<UserQuestionDTO>>

    @GET("api/questions/answered/{questionId}/{langCode}")
    suspend fun getAnsweredQuestion(@Path("questionId") questionId: Long, @Path("langCode") langCode: String): Response<AnsweredQuestionDTO>

}