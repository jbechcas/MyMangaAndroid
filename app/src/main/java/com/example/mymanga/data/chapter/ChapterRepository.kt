package com.example.mymanga.data.chapter

import com.example.mymanga.data.ApiService
import com.example.mymanga.data.network.ChapterRequest
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getChapters(): List<Chapter> {
        val response = apiService.getChapters()
        return response.data.map { strapiData ->
            Chapter(
                id = strapiData.id,
                title = strapiData.attributes.title ?: "Sin título",
                description = strapiData.attributes.description ?: "Sin descripción",
                mangaId = extractMangaId(strapiData.attributes.manga),
                mangaTitle = getMangaTitle(strapiData.attributes.manga)
            )
        }
    }

    suspend fun createChapter(chapter: Chapter): Chapter {
        val request = ChapterRequest(
            data = ChapterRequest.ChapterData(
                title = chapter.title,
                description = chapter.description,
                manga = chapter.mangaId
            )
        )

        val response = apiService.createChapter(request)

        return Chapter(
            id = response.data.id,
            title = response.data.attributes.title ?: "Sin título",
            description = response.data.attributes.description ?: "Sin descripción",
            mangaId = extractMangaId(response.data.attributes.manga),
            mangaTitle = getMangaTitle(response.data.attributes.manga)
        )
    }

    suspend fun updateChapter(id: Int, chapter: Chapter): Chapter {
        val request = ChapterRequest(
            data = ChapterRequest.ChapterData(
                title = chapter.title,
                description = chapter.description,
                manga = chapter.mangaId
            )
        )

        val response = apiService.updateChapter(id, request)

        return Chapter(
            id = response.data.id,
            title = response.data.attributes.title ?: "Sin título",
            description = response.data.attributes.description ?: "Sin descripción",
            mangaId = extractMangaId(response.data.attributes.manga),
            mangaTitle = getMangaTitle(response.data.attributes.manga)
        )
    }

    suspend fun deleteChapter(id: Int): ResponseBody {
        return apiService.deleteChapter(id)
    }

    private fun extractMangaId(manga: Any?): Int {
        if (manga == null) return 0
        try {
            if (manga is Map<*, *>) {
                val data = manga["data"]
                if (data is Map<*, *>) {
                    return (data["id"] as? Number)?.toInt() ?: 0
                }
            }
            return 0
        } catch (e: Exception) {
            return 0
        }
    }

    private fun getMangaTitle(manga: Any?): String {
        if (manga == null) return "Sin manga"
        try {
            if (manga is Map<*, *>) {
                val data = manga["data"]
                if (data is Map<*, *>) {
                    val attributes = data["attributes"]
                    if (attributes is Map<*, *>) {
                        return attributes["title"] as? String ?: "Sin manga"
                    }
                }
            }
            return "Sin manga"
        } catch (e: Exception) {
            return "Sin manga"
        }
    }
}