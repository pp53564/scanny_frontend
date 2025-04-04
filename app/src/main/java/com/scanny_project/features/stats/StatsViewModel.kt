package com.scanny_project.features.stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.utils.Result
import com.scanny_project.data.repository.AttemptsRepository
import com.scanny_project.data.model.StatsPerUserAndLanguageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val attemptsRepository: AttemptsRepository
) : ViewModel() {

    private val _languageStatsList = MutableLiveData<List<StatsPerUserAndLanguageDTO>>()
    val languageStatsList: LiveData<List<StatsPerUserAndLanguageDTO>> = _languageStatsList

    fun loadUserLanguageStats() {
        viewModelScope.launch {
            val result = attemptsRepository.getAllUserLanguageLectures()
            Log.i("petra1", result.toString())
                if (result is Result.Success) {
                    Log.i("petra2", result.data.toString())
                    if (result.data.isSuccessful) {
                        Log.i("petra3", result.data.body().toString())
                        result.data.body()?.let {
                            _languageStatsList.value = it
                        } ?: Log.e("StatsViewModel", "Stats body was null")
                    } else {
                        Log.e("StatsViewModel", "Response failed with code ${result.data.code()}")
                    }
                }
            if (result is Result.Error) {
                    Log.e("StatsViewModel", "Failed to load stats", result.exception)
                }
            }
    }
}
