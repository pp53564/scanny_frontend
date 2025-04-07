package com.scanny_project.features.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.model.UserQuestionDTO
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QuestionListViewModel @Inject constructor(
    private val lectureRepository: LectureRepository
) : ViewModel() {
    private val _questions = MutableLiveData<List<UserQuestionDTO>>()
    val questions: LiveData<List<UserQuestionDTO>> get() = _questions

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadQuestions(lectureId: Long, selectedLangCode: String) {
        viewModelScope.launch {
            val questionsResult = lectureRepository.getUserQuestionsByLectureAndLang(lectureId, selectedLangCode)
            if (questionsResult is Result.Success) {
                _questions.value = questionsResult.data
            } else {
                _errorMessage.value = "Failed to fetch questions."
            }
        }
    }
}