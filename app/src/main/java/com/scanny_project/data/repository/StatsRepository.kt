package com.scanny_project.data.repository

import com.scanny_project.data.model.NeighborDTO
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import com.scanny_project.utils.Result

interface StatsRepository {
    suspend fun getAllUserLanguageLectures(): Result<List<StatsPerUserAndLanguageDTO>>
    suspend fun getNeighborsForLanguage(selectedLanguage: String): Result<List<NeighborDTO>>
}