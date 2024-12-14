package com.scanny_project.data

import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.QuestionDTO
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

    suspend fun getQuestionsByLecture(lectureId: Long): Result<List<QuestionDTO>> {
        return try {
            val response = lectureService.getQuestionsByLecture(lectureId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching questions", e))
        }
    }
}
