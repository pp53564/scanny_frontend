package com.scanny_project.data.services

import com.scanny_project.data.model.CreateUserRequest
import com.scanny_project.data.model.AuthenticationResponse
import com.scanny_project.data.model.ChangePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users")
    suspend fun createOrLoginUser(@Body createUserRequest: CreateUserRequest): Response<AuthenticationResponse>
    @POST("users/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<String>

}