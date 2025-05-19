package com.example.mymanga.data.manga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val mangaRepository: MangaRepository) : ViewModel() {

    private val _mangas = MutableLiveData<List<Manga>>()
    val mangas: LiveData<List<Manga>> = _mangas

    private val _operationResult = MutableLiveData<Result<String>>()
    val operationResult: LiveData<Result<String>> = _operationResult

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadMangas()
    }

    fun loadMangas() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val mangaList = mangaRepository.getMangas()
                    .sortedBy { it.title.lowercase() }
                _mangas.value = mangaList
                if (mangaList.isEmpty()) {
                    _operationResult.value = Result.success("No se encontraron mangas")
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createManga(title: String, description: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newManga = Manga(title = title, description = description)
                mangaRepository.createManga(newManga)
                _operationResult.value = Result.success("Manga creado correctamente")
                loadMangas()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateManga(id: Int, title: String, description: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val updatedManga = Manga(id, title, description)
                mangaRepository.updateManga(id, updatedManga)
                _operationResult.value = Result.success("Manga actualizado correctamente")
                loadMangas()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteManga(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                mangaRepository.deleteManga(id)
                _operationResult.value = Result.success("Manga eliminado correctamente")
                loadMangas()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}