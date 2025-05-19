package com.example.mymanga.data.network

data class PersonRequest(val data: PersonData) {
    data class PersonData(
        val name: String,
        val surname: String,
        val users_permissions_user: Int? = null
    )
}