package com.scanny_project.data.repository

import com.scanny_project.data.model.LectureDTO
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.utils.Result

interface LectureRepository {

    suspend fun getAllLectures(): Result<List<LectureDTO>>
    suspend fun getAllUserLectures(): Result<List<UserLectureDTO>>

    suspend fun getAllUserLanguageLectures(selectedLangCode: String?): Result<List<UserLectureDTO>>

    suspend fun sendLecture(
        lectureName: String,
        items: List<ScannedItem>
    ): Result<String>
}
