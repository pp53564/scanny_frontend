package com.scanny_project.data.repository

import com.scanny_project.data.model.LoggedInUser
import com.scanny_project.utils.Result

interface UserRepository {
    suspend fun login(username: String, password: String): Result<LoggedInUser>

    fun logout()

    val isLoggedIn: Boolean
}