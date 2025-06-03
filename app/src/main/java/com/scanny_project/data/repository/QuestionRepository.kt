package com.scanny_project.data.repository

import com.scanny_project.data.model.AnsweredQuestionDTO
import com.scanny_project.data.model.QuestionDTO
import com.scanny_project.data.model.UserQuestionDTO
import com.scanny_project.utils.Result
import java.io.IOException

interface QuestionRepository {
    suspend fun getQuestionsByLecture(lectureId: Long): Result<List<QuestionDTO>>
    suspend fun getUserQuestionsByLecture(lectureId: Long): Result<List<UserQuestionDTO>>
    suspend fun getUserQuestionsByLectureAndLang(lectureId: Long, selectedLangCode: String): Result<List<UserQuestionDTO>>
    suspend fun getAnsweredQuestionByQuestionIdAndLang(questionId: Long, selectedLangCode: String): Result<AnsweredQuestionDTO>
}