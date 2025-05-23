package com.example.mymanga.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymanga.data.manga.Manga

@Entity(tableName = "mangas")
data class MangaEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String?
) {
    fun toManga(): Manga {
        return Manga(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl
        )
    }

    companion object {
        fun fromManga(manga: Manga): MangaEntity {
            return MangaEntity(
                id = manga.id,
                title = manga.title,
                description = manga.description,
                imageUrl = manga.imageUrl
            )
        }
    }
}