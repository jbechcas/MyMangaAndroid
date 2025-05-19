package com.example.mymanga.data.network

data class StrapiData<T>(
    val id: Int,
    val attributes: T
)