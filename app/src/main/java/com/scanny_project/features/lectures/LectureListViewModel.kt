package com.scanny_project.features.lectures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.UserLectureDTO
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val lectureRepository: LectureRepository
) : ViewModel() {
    private val _lectures = MutableLiveData<List<UserLectureDTO>>()
    val lectures: LiveData<List<UserLectureDTO>> get() = _lectures

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadLectures(selectedLangCode: String) {
        viewModelScope.launch {
            val lecturesResult = lectureRepository.getAllUserLanguageLectures(selectedLangCode)
            if (lecturesResult is Result.Success) {
                _lectures.value = lecturesResult.data
            } else {
                _errorMessage.value = "Failed to fetch lectures."
            }
        }
    }
}
