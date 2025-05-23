package com.example.mymanga

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mymanga.data.local.AppDatabase
import com.example.mymanga.data.local.dao.ChapterDao
import com.example.mymanga.data.local.dao.MangaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application()

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    @Singleton
    fun provideTestString(): String = "Hilt está funcionando correctamente"
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "manga_database"
        ).build()
    }

    @Provides
    fun provideMangaDao(database: AppDatabase): MangaDao {
        return database.mangaDao()
    }

    @Provides
    fun provideChapterDao(database: AppDatabase): ChapterDao {
        return database.chapterDao()
    }
}