package com.example.mymanga.data.manga

import com.example.mymanga.data.ApiService
import com.example.mymanga.data.local.dao.MangaDao
import com.example.mymanga.data.local.entities.MangaEntity
import com.example.mymanga.data.network.MangaRequest
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val apiService: ApiService,
    private val mangaDao: MangaDao
) {

    suspend fun getMangas(): List<Manga> {
        return try {
            // Intentar obtener datos de Strapi
            val response = apiService.getMangas()
            val mangasFromApi = response.data.map { strapiData ->
                Manga(
                    id = strapiData.id,
                    title = strapiData.attributes.title ?: "Sin título",
                    description = strapiData.attributes.description ?: "Sin descripción",
                    imageUrl = obtenerUrlImagen(strapiData.attributes.picture)
                )
            }

            // Si hay hay datos de strapi lo guardamos en room
            if (mangasFromApi.isNotEmpty()) {
                val entities = mangasFromApi.map { MangaEntity.fromManga(it) }
                mangaDao.clearMangas()
                mangaDao.insertMangas(entities)
                mangasFromApi
            } else {
                getMangasFromLocal()
            }
        } catch (e: Exception) {
            getMangasFromLocal()
        }
    }

    private suspend fun getMangasFromLocal(): List<Manga> {
        return mangaDao.getAllMangas().map { it.toManga() }
    }

    suspend fun createManga(manga: Manga): Manga {
        val request = MangaRequest(
            data = MangaRequest.MangaData(
                title = manga.title,
                description = manga.description
            )
        )

        val response = apiService.createManga(request)

        return Manga(
            id = response.data.id,
            title = response.data.attributes.title ?: "Sin título",
            description = response.data.attributes.description ?: "Sin descripción",
            imageUrl = null
        )
    }

    suspend fun updateManga(id: Int, manga: Manga): Manga {
        val request = MangaRequest(
            data = MangaRequest.MangaData(
                title = manga.title,
                description = manga.description
            )
        )

        val response = apiService.updateManga(id, request)

        return Manga(
            id = response.data.id,
            title = response.data.attributes.title ?: "Sin título",
            description = response.data.attributes.description ?: "Sin descripción",
            imageUrl = null
        )
    }

    suspend fun deleteManga(id: Int): ResponseBody {
        return apiService.deleteManga(id)
    }

    private fun obtenerUrlImagen(picture: Any?): String? {
        if (picture == null) return null
        try {
            if (picture is Map<*, *>) {
                val data = picture["data"]
                if (data is Map<*, *>) {
                    val attributes = data["attributes"]
                    if (attributes is Map<*, *>) {
                        return attributes["url"] as? String
                    }
                }
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }
}