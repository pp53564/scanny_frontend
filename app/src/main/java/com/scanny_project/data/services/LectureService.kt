package com.scanny_project.data.services

import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.QuestionDTO
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.model.UserQuestionDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LectureService {
    @GET("api/lectures")
    suspend fun getAllLectures(): Response<List<LectureDTO>>
    @GET("api/lectures/user")
    suspend fun getAllUserLectures(): Response<List<UserLectureDTO>>

    @GET("api/lectures/user/{selectedLangCode}")
    suspend fun getAllUserLanguageLectures(@Path("selectedLangCode") selectedLangCode: String?): Response<List<UserLectureDTO>>

}
