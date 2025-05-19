package com.example.mymanga.data

import com.example.mymanga.data.chapter.ChapterAttributes
import com.example.mymanga.data.network.ChapterRequest
import com.example.mymanga.data.network.LoginRequest
import com.example.mymanga.data.network.LoginResponse
import com.example.mymanga.data.manga.MangaAttributes
import com.example.mymanga.data.network.MangaRequest
import com.example.mymanga.data.register.RegisterRequest
import com.example.mymanga.data.register.RegisterResponse
import com.example.mymanga.data.network.StrapiResponse
import com.example.mymanga.data.network.StrapiSingleResponse
import com.example.mymanga.data.person.PersonAttributes
import com.example.mymanga.data.network.PersonRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Autenticación
    @POST("auth/local")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/local/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    // Mangas
    @GET("mangas")
    suspend fun getMangas(@Query("populate") populate: String = "picture"): StrapiResponse<MangaAttributes>

    @POST("mangas")
    suspend fun createManga(@Body request: MangaRequest): StrapiSingleResponse<MangaAttributes>

    @PUT("mangas/{id}")
    suspend fun updateManga(@Path("id") id: Int, @Body request: MangaRequest): StrapiSingleResponse<MangaAttributes>

    @DELETE("mangas/{id}")
    suspend fun deleteManga(@Path("id") id: Int): ResponseBody

    // Personas
    @GET("people")
    suspend fun getPeople(
        @Query("filters[users_permissions_user][id][\$eq]") userId: Int,
        @Query("populate") populate: String = "picture,users_permissions_user"
    ): StrapiResponse<PersonAttributes>

    @PUT("people/{id}")
    suspend fun updatePerson(@Path("id") id: Int, @Body request: PersonRequest): StrapiSingleResponse<PersonAttributes>

    @POST("people")
    suspend fun createPerson(@Body request: PersonRequest): StrapiSingleResponse<PersonAttributes>

    // Capitulos
    @GET("chapters")
    suspend fun getChapters(@Query("populate") populate: String = "manga"): StrapiResponse<ChapterAttributes>

    @POST("chapters")
    suspend fun createChapter(@Body request: ChapterRequest): StrapiSingleResponse<ChapterAttributes>

    @PUT("chapters/{id}")
    suspend fun updateChapter(@Path("id") id: Int, @Body request: ChapterRequest): StrapiSingleResponse<ChapterAttributes>

    @DELETE("chapters/{id}")
    suspend fun deleteChapter(@Path("id") id: Int): ResponseBody
}