package com.scanny_project.features.teacher.lecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.ScannedItem
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.utils.CustomResult
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddLectureViewModel @Inject constructor(
    private val repo: LectureRepository
) : ViewModel() {

    private var lectureName = MutableLiveData("")
    private val selectedItems = MutableLiveData<List<ScannedItem>>(emptyList())
    private val _scannedItems = MutableLiveData<List<ScannedItem>>(emptyList())
    val scannedItems: LiveData<List<ScannedItem>> = _scannedItems

//    fun setItems(items: List<ScannedItem>) {
//        _scannedItems.value = items
//    }

    fun setItems(items: List<ScannedItem>) {
        // Take the current list (or emptyList if null),
        // convert to mutable, then addAll(new items)
        val updated = _scannedItems.value
            .orEmpty()
            .toMutableList()
            .apply { addAll(items) }

        _scannedItems.value = updated
    }


    fun addItem(item: ScannedItem) {
        val allItems = _scannedItems.value.orEmpty()
        if (allItems.any { it.translations["en"] == item.translations["en"]  ||
                    it.translations["hr"] == item.name
            }) {
            _uiState.value = UiState.Error("Predmet \"${item.name}\" je već u listi.")
        } else {
            val updated = allItems.toMutableList().apply { add(item) }
            _scannedItems.value = updated
        }
    }

    fun updateItem(position: Int, item: ScannedItem) {
        val updated = _scannedItems.value.orEmpty().toMutableList().apply {
            if (position in indices) {
                this[position] = item
            }
        }
        _scannedItems.value = updated
    }

    fun getSelectedItems(): List<ScannedItem> =
        _scannedItems.value.orEmpty().filter { it.isChecked }

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
        val name = lectureName.value.orEmpty().trim()
        val items = selectedItems.value.orEmpty()

        val nameErr = if (name.isEmpty()) "Unesite naziv lekcije" else null
        val itemsErr = if (items.isEmpty()) "Odaberite bar jedan predmet" else null
        if (nameErr != null || itemsErr != null) {
            _uiState.value = UiState.ValidationError(nameErr, itemsErr)
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
//            try {
//                val result = repo.sendLecture(name, items)
//                Log.i("AddLectureViewModel", "sendLecture result: $result")
//                if (result is Result.Success) {
//                    _uiState.value = UiState.Success(result.data)
//                } else {
//                    if(Result.HttpError) {
//                        if(result.code == 409) _uiState.value = UiState.Error("Naziv lekcije je već zauzet.")
//                    } else {
//                        _uiState.value = UiState.Error("Greška pri slanju lekcije")
//                    }
//                }
//            } catch (e: IOException) {
//                _uiState.value = UiState.Error(e.localizedMessage ?: "Greška u mreži")
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error("Neočekivana greška")
//            }

            when (val result = repo.sendLecture(name, items)) {
                is CustomResult.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }

                is CustomResult.HttpError -> {
                    if (result.code == 409) {
                        _uiState.value = UiState.Error("Naziv lekcije je već zauzet.")
                    } else {
                        _uiState.value = UiState.Error("Greška pri slanju lekcije (HTTP ${result.code})")
                    }
                }

                is CustomResult.Error -> {
                    _uiState.value = UiState.Error("Ne mogu se povezati na server.")
                }
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