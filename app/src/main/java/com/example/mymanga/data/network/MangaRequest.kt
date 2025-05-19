package com.example.mymanga.data.network

data class MangaRequest(val data: MangaData) {
    data class MangaData(
        val title: String,
        val description: String
    )
}