package com.scanny_project.data.services

import com.scanny_project.data.model.NeighborDTO
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface StatsService {
    @GET("api/stats/user")
    suspend fun getUserLanguagesStats(): List<StatsPerUserAndLanguageDTO>

    @GET("api/stats/user/{selectedLangCode}")
    suspend fun getNeighborsForLanguage(@Path("selectedLangCode") selectedLangCode: String): List<NeighborDTO>

}