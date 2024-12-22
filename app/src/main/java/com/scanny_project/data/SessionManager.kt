package com.scanny_project.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefsName = "user_session"
    private val authTokenKey = "auth_token"
    private val userIdKey = "user_id"

    private val sharedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            prefsName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var authToken: String?
        get() = sharedPreferences.getString(authTokenKey, null)
        set(value) {
            sharedPreferences.edit().putString(authTokenKey, value).apply()
        }

    var userId: Long
        get() = sharedPreferences.getLong(userIdKey, 0L)
        set(value) {
            sharedPreferences.edit().putLong(userIdKey, value).apply()
        }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}