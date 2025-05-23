package com.example.mymanga.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymanga.data.chapter.Chapter

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val mangaId: Int,
    val mangaTitle: String
) {
    fun toChapter(): Chapter {
        return Chapter(
            id = id,
            title = title,
            description = description,
            mangaId = mangaId,
            mangaTitle = mangaTitle
        )
    }

    companion object {
        fun fromChapter(chapter: Chapter): ChapterEntity {
            return ChapterEntity(
                id = chapter.id,
                title = chapter.title,
                description = chapter.description,
                mangaId = chapter.mangaId,
                mangaTitle = chapter.mangaTitle
            )
        }
    }
}