package com.scanny_project.data.api

import com.scanny_project.data.model.CreateLectureRequest
import com.scanny_project.data.model.LectureCreatedDto
import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.UserLectureDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LectureApi {
    @GET("api/lectures")
    suspend fun getAllLectures(): Response<List<LectureDTO>>
    @GET("api/lectures/user")
    suspend fun getAllUserLectures(): Response<List<UserLectureDTO>>

    @GET("api/lectures/user/{selectedLangCode}")
    suspend fun getAllUserLanguageLectures(@Path("selectedLangCode") selectedLangCode: String?): Response<List<UserLectureDTO>>

    @POST("api/lectures/teacher/createLecture")
    suspend fun createNewLecture(@Body createLectureRequest: CreateLectureRequest): Response<LectureCreatedDto>

}
