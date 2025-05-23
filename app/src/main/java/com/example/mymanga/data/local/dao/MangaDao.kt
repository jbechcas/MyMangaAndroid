package com.example.mymanga.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymanga.data.local.entities.MangaEntity

@Dao
interface MangaDao {
    @Query("SELECT * FROM mangas ORDER BY title ASC")
    suspend fun getAllMangas(): List<MangaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(mangas: List<MangaEntity>)

    @Query("DELETE FROM mangas")
    suspend fun clearMangas()
}