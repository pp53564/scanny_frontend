package com.scanny_project.data

import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.QuestionDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LectureService {
    @GET("api/lectures")
    suspend fun getAllLectures(): Response<List<LectureDTO>>

    @GET("api/questions/lecture/{lectureId}")
    suspend fun getQuestionsByLecture(@Path("lectureId") lectureId: Long): Response<List<QuestionDTO>>
}
