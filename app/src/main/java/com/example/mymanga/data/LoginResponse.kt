package com.example.mymanga.data

data class LoginResponse(
    val jwt: String,
    val user: User
)
