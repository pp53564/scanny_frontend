package com.scanny_project

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.AttemptsRepository
import com.scanny_project.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.scanny_project.data.Result

@HiltViewModel
class ImageQuizViewModel @Inject constructor(
    private val attemptsRepository: AttemptsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _attemptSent = MutableLiveData<Boolean>()
    val attemptSent: LiveData<Boolean> get() = _attemptSent

    init {
        _attemptSent.value = false
    }

    fun sendAttempt(questionId: Long, succeeded: Boolean, imageBitmap: Bitmap?) {
        if (_attemptSent.value == true) return

        _attemptSent.value = true
        val userId = sessionManager.userId

        viewModelScope.launch {
            val result = attemptsRepository.recordAttempt(userId, questionId, succeeded, imageBitmap)
            if (result is Result.Success) {
                Log.i("ImageQuizViewModel", "Attempt recorded successfully.")
            } else {
                Log.e("ImageQuizViewModel", "Failed to record attempt.")
            }
        }
    }
}
