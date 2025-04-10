package com.scanny_project.data.repository

import android.util.Log
import com.scanny_project.data.model.NeighborDTO
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import com.scanny_project.data.services.StatsService
import com.scanny_project.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val statsService: StatsService
) {
    suspend fun getAllUserLanguageLectures(): Result<List<StatsPerUserAndLanguageDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val statsList = statsService.getUserLanguagesStats()
                Log.i("StatsRepository", "getAllUserLanguageLectures: Success")
                Log.i("StatsRepository", statsList.toString())
                Result.Success(statsList)
            } catch (e: Exception) {
                Log.e("StatsRepository", "getAllUserLanguageLectures: Exception - ${e.message}", e)
                Result.Error(e)
            }
        }
    }

    suspend fun getNeighborsForLanguage(selectedLanguage: String): Result<List<NeighborDTO>> {
        return withContext(Dispatchers.IO) {
            try {
                val statsList = statsService.getNeighborsForLanguage(selectedLanguage)
                Log.i("StatsRepository", "getNeighborsForLanguage: Success")
                Log.i("StatsRepository", statsList.toString())
                Result.Success(statsList)
            } catch (e: Exception) {
                Log.e("StatsRepository", "getNeighborsForLanguage: Exception - ${e.message}", e)
                Result.Error(e)
            }
        }
    }
}