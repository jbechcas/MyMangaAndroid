package com.example.mymanga.data.network
import com.example.mymanga.data.User

data class LoginResponse(val jwt: String, val user: User)