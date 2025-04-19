package com.scanny_project.features.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.AnsweredQuestionDTO
import com.scanny_project.data.repository.QuestionRepository
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnsweredQuestionViewModel @Inject constructor(
    private val repo: QuestionRepository
) : ViewModel() {

    private val _question = MutableLiveData<AnsweredQuestionDTO>()
    val question: LiveData<AnsweredQuestionDTO> get() = _question

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun load(questionId: Long, langCode: String) = viewModelScope.launch {
            val questionResult = repo.getAnsweredQuestionByQuestionIdAndLang(questionId, langCode)
            if (questionResult is Result.Success) {
                _question.value = questionResult.data
            } else {
                _errorMessage.value = "Failed to fetch questions."
            }
    }
}




