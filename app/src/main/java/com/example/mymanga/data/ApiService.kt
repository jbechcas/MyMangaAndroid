package com.example.mymanga.data

import com.example.mymanga.data.network.LoginRequest
import com.example.mymanga.data.network.LoginResponse
import com.example.mymanga.data.network.RegisterRequest
import com.example.mymanga.data.network.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/local")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/local/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}
