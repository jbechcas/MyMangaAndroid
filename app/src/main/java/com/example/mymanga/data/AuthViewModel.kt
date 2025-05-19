package com.example.mymanga.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymanga.data.network.LoginResponse
import com.example.mymanga.data.register.RegisterResponse
import com.example.mymanga.data.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    private val _isLoggedIn = MutableLiveData<Boolean>()

    init {
        _isLoggedIn.value = sessionManager.isLoggedIn()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(username, password)
                sessionManager.saveAuthUser(response.jwt, response.user)
                _loginResult.value = Result.success(response)
                _isLoggedIn.value = true

            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }


    fun register(username: String, email: String, password: String, name: String, surname: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.register(username, email, password, name, surname)
                _registerResult.value = Result.success(response)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _isLoggedIn.value = false
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

}