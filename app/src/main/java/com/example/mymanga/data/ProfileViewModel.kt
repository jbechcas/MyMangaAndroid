package com.example.mymanga.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymanga.data.person.Person
import com.example.mymanga.data.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileData = MutableLiveData<Person?>()
    val profileData: LiveData<Person?> = _profileData

    private val _operationResult = MutableLiveData<Result<String>>()
    val operationResult: LiveData<Result<String>> = _operationResult

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId()

                if (userId > 0) {
                    val person = personRepository.getPersonByUserId(userId)
                    _profileData.value = person
                } else {
                    _operationResult.value = Result.failure(Exception("Usuario no autenticado"))
                }

            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(name: String, surname: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId()

                if (userId > 0) {
                    val currentProfile = _profileData.value

                    if (currentProfile != null) {
                        val updatedPerson = currentProfile.copy(name = name, surname = surname)
                        val result = personRepository.updatePerson(currentProfile.id, updatedPerson)
                        _profileData.value = result
                        _operationResult.value = Result.success("Perfil actualizado correctamente")
                    } else {
                        _operationResult.value = Result.failure(Exception("Perfil no encontrado"))
                    }
                } else {
                    _operationResult.value = Result.failure(Exception("Usuario no autenticado"))
                }

            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserName(): String? {
        return sessionManager.getUserName()
    }

    fun getUserEmail(): String? {
        return sessionManager.getUserEmail()
    }
}