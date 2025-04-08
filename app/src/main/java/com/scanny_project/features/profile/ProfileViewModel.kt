package com.scanny_project.features.profile

import androidx.lifecycle.ViewModel
import com.scanny_project.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun logout() {
        userRepository.logout()
    }
}
