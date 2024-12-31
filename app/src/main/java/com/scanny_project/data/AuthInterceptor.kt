package com.scanny_project.data

import android.content.Intent
import com.scanny_project.MyApp
import com.scanny_project.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = sessionManager.authToken

        if (token != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }
        val response = chain.proceed(request)
        if (response.code == 403) {
            sessionManager.clearSession()
            val context = MyApp.getAppContext()
            val loginIntent = Intent(context, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(loginIntent)
        }
        return response

    }
}
