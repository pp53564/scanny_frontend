package com.scanny_project.data.repository.impl

import com.scanny_project.data.model.CreateUserRequest
import com.scanny_project.data.model.LoggedInUser
import com.scanny_project.data.SessionManager
import com.scanny_project.data.api.UserApi
import com.scanny_project.data.repository.UserRepository
import com.scanny_project.utils.Result
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val sessionManager: SessionManager
) : UserRepository {

    // keep current user in memory
    private var currentUser: LoggedInUser? = null

    override val isLoggedIn: Boolean
        get() = currentUser != null

    override suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val request = CreateUserRequest(username, password)
            val response = api.createOrLoginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                val user = LoggedInUser(
                    displayName = username,
                    token       = auth.token,
                    id          = auth.id,
                    role        = auth.role
                )
                sessionManager.userId    = user.id
                sessionManager.authToken = user.token
                sessionManager.userRole  = user.role

                currentUser = user
                Result.Success(user)
            } else {
                Result.Error(IOException("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override fun logout() {
        // clear local session
        sessionManager.clearSession()
        currentUser = null
    }
}
