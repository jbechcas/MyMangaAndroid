package com.example.mymanga.data.chapter

data class Chapter(
    val id: Int = 0,
    val title: String = "Sin ningún título",
    val description: String = "Sin ninguna descripción",
    val mangaId: Int = 0,
    val mangaTitle: String = "Sin manga"
)