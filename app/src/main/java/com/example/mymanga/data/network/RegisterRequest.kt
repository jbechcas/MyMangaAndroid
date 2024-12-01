package com.example.mymanga.data.network

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmed: Boolean = true // Asegúrate de incluir esto para marcar al usuario como confirmado
)

