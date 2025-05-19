package com.example.mymanga.data.network

data class StrapiResponse<T>(
    val data: List<StrapiData<T>>,
)