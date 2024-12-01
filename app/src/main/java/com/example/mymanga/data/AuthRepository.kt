package com.example.mymanga.data

import com.example.mymanga.data.network.LoginRequest
import com.example.mymanga.data.network.LoginResponse
import com.example.mymanga.data.network.RegisterRequest
import com.example.mymanga.data.network.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(username: String, password: String): LoginResponse {
        val request = LoginRequest(identifier = username, password = password)
        return apiService.login(request)
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        val request = RegisterRequest(username, email, password)
        return apiService.register(request)
    }
}