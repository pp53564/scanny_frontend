package com.scanny_project.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui_ux_demo.R
import com.scanny_project.data.LoginRepository
import com.scanny_project.data.Result
import com.scanny_project.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository, private val sessionManager: SessionManager) : ViewModel(){

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _protectedResourceResult = MutableLiveData<String>()
    val protectedResourceResult: LiveData<String> = _protectedResourceResult


    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                sessionManager.authToken = result.data.token
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayUserName = result.data.displayName))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }


    fun loginDataChanged(username: String, password: String) {
      if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }

}