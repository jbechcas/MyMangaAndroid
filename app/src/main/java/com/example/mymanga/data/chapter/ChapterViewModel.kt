package com.example.mymanga.data.chapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymanga.data.manga.Manga
import com.example.mymanga.data.manga.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val mangaRepository: MangaRepository
) : ViewModel() {

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>> = _chapters
    private val _mangas = MutableLiveData<List<Manga>>()
    val mangas: LiveData<List<Manga>> = _mangas
    private val _operationResult = MutableLiveData<Result<String>>()
    val operationResult: LiveData<Result<String>> = _operationResult
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadChapters()
        loadMangas()
    }

    fun loadChapters() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val chapterList = chapterRepository.getChapters()
                    .sortedBy { it.title.lowercase() }
                _chapters.value = chapterList
                if (chapterList.isEmpty()) {
                    _operationResult.value = Result.success("No se encontraron capítulos")
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMangas() {
        viewModelScope.launch {
            try {
                val mangaList = mangaRepository.getMangas()
                    .sortedBy { it.title.lowercase() }
                _mangas.value = mangaList
            } catch (e: Exception) {
            }
        }
    }

    fun createChapter(title: String, description: String, mangaId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newChapter = Chapter(
                    title = title,
                    description = description,
                    mangaId = mangaId
                )
                chapterRepository.createChapter(newChapter)
                _operationResult.value = Result.success("Capítulo creado correctamente")
                loadChapters()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateChapter(id: Int, title: String, description: String, mangaId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val updatedChapter = Chapter(
                    id = id,
                    title = title,
                    description = description,
                    mangaId = mangaId
                )
                chapterRepository.updateChapter(id, updatedChapter)
                _operationResult.value = Result.success("Capítulo actualizado correctamente")
                loadChapters()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteChapter(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                chapterRepository.deleteChapter(id)
                _operationResult.value = Result.success("Capítulo eliminado correctamente")
                loadChapters()
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}