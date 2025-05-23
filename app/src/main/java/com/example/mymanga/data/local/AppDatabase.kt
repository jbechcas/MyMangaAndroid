package com.example.mymanga.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymanga.data.local.dao.ChapterDao
import com.example.mymanga.data.local.dao.MangaDao
import com.example.mymanga.data.local.entities.ChapterEntity
import com.example.mymanga.data.local.entities.MangaEntity

@Database(
    entities = [MangaEntity::class, ChapterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mangaDao(): MangaDao
    abstract fun chapterDao(): ChapterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
    }
}