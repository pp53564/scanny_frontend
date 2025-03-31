package com.scanny_project.data.services

import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import com.scanny_project.data.model.UserLectureDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StatsService {
    @GET("api/stats/user")
    suspend fun getUserLanguagesStats(): Response<List<StatsPerUserAndLanguageDTO>>

}