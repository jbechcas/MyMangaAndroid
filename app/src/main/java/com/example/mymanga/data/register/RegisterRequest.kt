package com.example.mymanga.data.register

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmed: Boolean = true
)

