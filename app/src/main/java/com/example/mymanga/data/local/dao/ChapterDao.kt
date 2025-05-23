package com.example.mymanga.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymanga.data.local.entities.ChapterEntity

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters ORDER BY title ASC")
    suspend fun getAllChapters(): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Query("DELETE FROM chapters")
    suspend fun clearChapters()
}