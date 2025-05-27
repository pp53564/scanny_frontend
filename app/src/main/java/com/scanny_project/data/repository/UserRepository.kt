package com.scanny_project.data.repository

import com.scanny_project.utils.Result
import com.scanny_project.data.SessionManager
import com.scanny_project.data.model.LoggedInUser

class UserRepository(private val dataSource: UserDataSource, private val sessionManager: SessionManager) {
    private var user: LoggedInUser? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        sessionManager.clearSession()
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun changePassword(newPassword: String, oldPassword: String): Result<String> {
        return dataSource.changePassword(newPassword, oldPassword)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        sessionManager.userId = loggedInUser.id
        sessionManager.authToken = loggedInUser.token
        sessionManager.userRole = loggedInUser.role
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}