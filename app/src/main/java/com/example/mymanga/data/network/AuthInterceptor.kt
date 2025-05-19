package com.example.mymanga.data.network

import com.example.mymanga.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionManager.fetchAuthToken()

        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}