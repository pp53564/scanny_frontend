package com.scanny_project.data

import android.util.Log
import com.scanny_project.data.model.LoggedInUser
import com.scanny_project.data.services.UserService
import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val userService: UserService
) {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            Log.d("LoginDataSource", "Starting login network request")
            val createUserRequest = CreateUserRequest(username, password)
            val response = userService.createOrLoginUser(createUserRequest)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                val loggedInUser = LoggedInUser(
                    displayName = username,
                    token = authResponse.token,
                    id = authResponse.id
                )
                Log.d("LoginDataSource", "Login network request successful")
                Result.Success(loggedInUser)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("LoginDataSource", "Login failed: ${response.code()} ${response.message()} - $errorBody")
                Result.Error(IOException("Error logging in: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("LoginDataSource", "Exception during login network request", e)
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
