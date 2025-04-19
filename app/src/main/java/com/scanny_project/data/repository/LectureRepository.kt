package com.scanny_project.data.repository

import com.scanny_project.utils.Result
import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.QuestionDTO
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.model.UserQuestionDTO
import com.scanny_project.data.services.LectureService
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LectureRepository @Inject constructor(
    private val lectureService: LectureService
) {
    suspend fun getAllLectures(): Result<List<LectureDTO>> {
        return try {
            val response = lectureService.getAllLectures()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch lectures: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching lectures", e))
        }
    }
    suspend fun getAllUserLectures(): Result<List<UserLectureDTO>> {
        return try {
            val response = lectureService.getAllUserLectures()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch lectures: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching lectures", e))
        }
    }

    suspend fun getAllUserLanguageLectures(selectedLangCode: String?): Result<List<UserLectureDTO>> {
        return try {
            val response = lectureService.getAllUserLanguageLectures(selectedLangCode)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch lectures: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching lectures", e))
        }
    }
}
