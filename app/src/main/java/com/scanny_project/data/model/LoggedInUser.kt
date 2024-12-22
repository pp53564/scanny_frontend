package com.scanny_project.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val displayName: String,
    val token: String,
    val id: Long
)