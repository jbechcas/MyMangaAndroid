package com.example.mymanga.data.network

import com.example.mymanga.data.User

data class RegisterResponse(val jwt: String, val user: User)
