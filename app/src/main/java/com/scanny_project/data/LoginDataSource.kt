package com.scanny_project.data

import android.util.Log
import com.scanny_project.data.model.LoggedInUser
import java.io.IOException

class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            Log.d("LoginDataSource", "Starting login network request")
            val createUserRequest = CreateUserRequest(username, password)
            val response = RetrofitInstance.userService.createOrLoginUser(createUserRequest)

            Log.d("LoginDataSource", "Received response with code: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val userDTO = response.body()!!
                val loggedInUser = LoggedInUser(userDTO.id.toString(), userDTO.username)
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
