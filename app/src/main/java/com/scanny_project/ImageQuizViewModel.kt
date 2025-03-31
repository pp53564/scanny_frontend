package com.scanny_project

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scanny_project.data.AttemptsRepository
import com.scanny_project.data.Result
import com.scanny_project.data.SessionManager
import com.scanny_project.data.model.AttemptResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope

@HiltViewModel
class ImageQuizViewModel @Inject constructor(
    private val attemptsRepository: AttemptsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _attemptSent = MutableLiveData<Boolean>()
    val attemptSent: LiveData<Boolean> get() = _attemptSent

    private val _attemptResponse = MutableLiveData<AttemptResponse?>()
    val attemptResponse: LiveData<AttemptResponse?> get() = _attemptResponse

    init {
        _attemptSent.value = false
    }

    fun sendAttempt(questionId: Long, imageBitmap: Bitmap?, langCode: String) {
        if (_attemptSent.value == true) return

        _attemptSent.value = true

        GlobalScope.launch {
            val result = attemptsRepository.recordAttempt(questionId, imageBitmap, langCode)
            if (result is Result.Success) {
                _attemptResponse.postValue(result.data)
                Log.i("ImageQuizViewModel", "Attempt recorded successfully.")
            } else {
                Log.e("ImageQuizViewModel", "Failed to record attempt.")
            }
        }
    }
}
