package com.scanny_project.data.repository.impl

import com.scanny_project.data.api.QuestionApi
import com.scanny_project.data.model.AnsweredQuestionDTO
import com.scanny_project.data.model.QuestionDTO
import com.scanny_project.data.model.UserQuestionDTO
import com.scanny_project.data.repository.QuestionRepository
import com.scanny_project.utils.Result
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepositoryImpl @Inject constructor(
    private val api: QuestionApi
): QuestionRepository {
    override suspend fun getQuestionsByLecture(lectureId: Long): Result<List<QuestionDTO>> {
        return try {
            val response = api.getQuestionsByLecture(lectureId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching questions", e))
        }
    }
    override suspend fun getUserQuestionsByLecture(lectureId: Long): Result<List<UserQuestionDTO>> {
        return try {
            val response = api.getUserQuestionsByLecture(lectureId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching questions", e))
        }
    }
    override suspend fun getUserQuestionsByLectureAndLang(lectureId: Long, selectedLangCode: String): Result<List<UserQuestionDTO>> {
        return try {
            val response = api.getUserQuestionsByLectureAndLang(lectureId, selectedLangCode)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch questions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching questions", e))
        }
    }

    override suspend fun getAnsweredQuestionByQuestionIdAndLang(questionId: Long, selectedLangCode: String): Result<AnsweredQuestionDTO> {
        return try {
            val response = api.getAnsweredQuestion(questionId, selectedLangCode)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException("Failed to fetch question: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching question", e))
        }
    }
}