package com.scanny_project.features.teacher.lecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddLectureViewModel @Inject constructor(
    private val repo: LectureRepository
): ViewModel() {
    private var lectureName   = MutableLiveData("")
    private val selectedItems = MutableLiveData<List<ScannedItem>>(emptyList())

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data class Success(val lectureId: String) : UiState()
        data class ValidationError(
            val nameError: String?,
            val itemsError: String?
        ) : UiState()
        data class Error(val message: String) : UiState()
    }

    fun createLecture() {
        val name  = lectureName.value.orEmpty().trim()
        val items = selectedItems.value.orEmpty()

        val nameErr  = if (name.isEmpty()) "Unesite naziv lekcije" else null
        val itemsErr = if (items.isEmpty()) "Odaberite bar jedan predmet" else null
        if (nameErr != null || itemsErr != null) {
            _uiState.value = UiState.ValidationError(nameErr, itemsErr)
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repo.sendLecture(name, items)
                Log.i("petra1", "here")
                if (result is Result.Success) {
                    Log.i("petra", "here")
                    _uiState.value = UiState.Success(result.data)
                } else {
                    _uiState.value = UiState.Error("Greška pri slanju lekcije")
                }
            } catch (e: IOException) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Greška u mreži")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Neočekivana greška")
            }
        }
    }

    fun setLectureName(name: String) {
        lectureName.value = name
    }

    fun setSelectedItems(items: List<ScannedItem>) {
        selectedItems.value = items
    }
}