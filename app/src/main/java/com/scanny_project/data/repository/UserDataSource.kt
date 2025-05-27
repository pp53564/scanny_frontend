package com.scanny_project.data.repository

import android.util.Log
import com.scanny_project.data.model.ChangePasswordRequest
import com.scanny_project.data.model.CreateUserRequest
import com.scanny_project.utils.Result
import com.scanny_project.data.model.LoggedInUser
import com.scanny_project.data.services.UserService
import java.io.IOException
import javax.inject.Inject

class UserDataSource @Inject constructor(
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
                    id = authResponse.id,
                    role = authResponse.role
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

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<String> {
        return try {
            Log.d("UserDataSource", "Starting changePassword network request")
            val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword)
            val response = userService.changePassword(changePasswordRequest)

            if (response.isSuccessful && response.body() != null) {
                Log.d("UserDataSource", "Password changed successfully")
                Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("UserDataSource", "Change password failed: ${response.code()} ${response.message()} - $errorBody")
                Result.Error(IOException("Error changing password: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("UserDataSource", "Exception during changePassword network request", e)
            Result.Error(IOException("Error changing password", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
