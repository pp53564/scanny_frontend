package com.scanny_project.data.api

import com.scanny_project.data.model.CreateUserRequest
import com.scanny_project.data.model.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users")
    suspend fun createOrLoginUser(@Body createUserRequest: CreateUserRequest): Response<AuthenticationResponse>
}