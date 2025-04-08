package com.scanny_project.features.changePassword


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanny_project.data.model.ChangePasswordResult
import com.scanny_project.utils.Result
import com.scanny_project.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _changePasswordResult = MutableLiveData<ChangePasswordResult>()
    val changePasswordResult: LiveData<ChangePasswordResult>
        get() = _changePasswordResult

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            val result = userRepository.changePassword(oldPassword, newPassword)
            if (result is Result.Success) {
                _changePasswordResult.value =
                    ChangePasswordResult(success = result.data)
            } else if (result is Result.Error) {
                _changePasswordResult.value =
                    ChangePasswordResult(error = result.exception.localizedMessage)
            }
        }
    }
}
