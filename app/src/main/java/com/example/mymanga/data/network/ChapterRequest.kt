package com.example.mymanga.data.network

data class ChapterRequest(val data: ChapterData) {
    data class ChapterData(
        val title: String,
        val description: String,
        val manga: Int
    )
}