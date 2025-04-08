package com.scanny_project.data.model

data class ChangePasswordRequest(
    val newPassword: String,
    val oldPassword: String
)
