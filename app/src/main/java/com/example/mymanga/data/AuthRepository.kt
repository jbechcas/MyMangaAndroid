package com.example.mymanga.data

import com.example.mymanga.data.network.LoginRequest
import com.example.mymanga.data.network.LoginResponse
import com.example.mymanga.data.register.RegisterRequest
import com.example.mymanga.data.register.RegisterResponse
import com.example.mymanga.data.person.PersonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val personRepository: PersonRepository
) {

    suspend fun login(username: String, password: String): LoginResponse
    {
        val request = LoginRequest(identifier = username, password = password)
        return apiService.login(request)
    }

    suspend fun register(username: String, email: String, password: String, name: String, surname: String): RegisterResponse
    {

        val request = RegisterRequest(username, email, password)
        val response = apiService.register(request)
        personRepository.createProfile(response.user.id, name, surname)

        return response
    }
}