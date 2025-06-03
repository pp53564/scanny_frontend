package com.scanny_project.data.repository.impl

import com.scanny_project.data.api.LectureApi
import com.scanny_project.data.model.CreateLectureRequest
import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.data.model.TranslatedItemDto
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.utils.CustomResult
import com.scanny_project.utils.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LectureRepositoryImpl @Inject constructor(
    private val api: LectureApi
) : LectureRepository {

    override suspend fun getAllLectures(): Result<List<LectureDTO>> {
        return try {
            val response = api.getAllLectures()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch lectures: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching all lectures", e))
        }
    }

    override suspend fun getAllUserLectures(): Result<List<UserLectureDTO>> {
        return try {
            val response = api.getAllUserLectures()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch user lectures: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching user lectures", e))
        }
    }

    override suspend fun getAllUserLanguageLectures(
        selectedLangCode: String?
    ): Result<List<UserLectureDTO>> {
        return try {
            val response = api.getAllUserLanguageLectures(selectedLangCode)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(IOException("Failed to fetch language lectures: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching lectures for language $selectedLangCode", e))
        }
    }
    override suspend fun sendLecture(
        lectureName: String,
        items: List<ScannedItem>
    ): CustomResult<String> {
        return try {
            val dtoItems = items.map { item ->
                TranslatedItemDto(
                    base = item.name,
                    translations = item.translations
                )
            }

            val request = CreateLectureRequest(lectureName, dtoItems)

            val response = api.createNewLecture(request)

            if (!response.isSuccessful) {
                val code = response.code()
                val bodyString = response.errorBody()?.string()
                return CustomResult.HttpError(code, bodyString)
            }

            val body = response.body()
                ?:
                return CustomResult.Error(
                    Exception("Response body was null on success.")
                )

            CustomResult.Success(body.message)

        } catch (e: Exception) {
            CustomResult.Error(e)
        }
    }
}

