package com.scanny_project.utils

/**
 * A sealed‐class wrapper for repository results.
 *
 *   Success<T>        → when a call succeeded (body‐type T)
 *   HttpError         → when the server returned a non‐2xx (e.g. 409, 500)
 *   Error             → when a network/JSON/etc exception occurred
 */
sealed class CustomResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : CustomResult<T>()
    data class HttpError(
        val code: Int,
        val errorBody: String? = null
    ) : CustomResult<Nothing>()

    data class Error(val exception: Exception) : CustomResult<Nothing>()
}
