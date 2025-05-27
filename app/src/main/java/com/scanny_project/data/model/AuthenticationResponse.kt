package com.scanny_project.data.model

data class AuthenticationResponse(
    val token: String,
    val id: Long,
    val role: String
)

