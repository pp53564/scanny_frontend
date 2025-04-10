package com.scanny_project.features.stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.NeighborDTO
import com.scanny_project.data.repository.StatsRepository
import com.scanny_project.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsDetailsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _neighborsForLanguageList = MutableLiveData<List<NeighborDTO>>()
    val neighborsForLanguageList: LiveData<List<NeighborDTO>> = _neighborsForLanguageList

    fun loadNeighborsForLanguage(selectedLanguage: String) {
        viewModelScope.launch {
            val result = statsRepository.getNeighborsForLanguage(selectedLanguage)
            if (result is Result.Success) {
                _neighborsForLanguageList.value = result.data
            }
            if (result is Result.Error) {
                Log.e("StatsDetailsViewModel", "Failed to load stats", result.exception)
            }
        }
    }
}
