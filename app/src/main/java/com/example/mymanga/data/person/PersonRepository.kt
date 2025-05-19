package com.example.mymanga.data.person

import com.example.mymanga.data.ApiService
import com.example.mymanga.data.network.PersonRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPersonByUserId(userId: Int): Person? {
        try {
            val response = apiService.getPeople(userId = userId, populate = "picture,users_permissions_user")

            if (response.data.isEmpty()) {
                return null
            }

            val personData = response.data[0]
            return Person(
                id = personData.id,
                name = personData.attributes.name ?: "",
                surname = personData.attributes.surname ?: "",
                imageUrl = obtenerUrlImagen(personData.attributes.picture),
                userId = userId
            )
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updatePerson(id: Int, person: Person): Person {
        val request = PersonRequest(
            data = PersonRequest.PersonData(
                name = person.name,
                surname = person.surname
            )
        )

        val response = apiService.updatePerson(id, request)

        return Person(
            id = response.data.id,
            name = response.data.attributes.name ?: "",
            surname = response.data.attributes.surname ?: "",
            imageUrl = obtenerUrlImagen(response.data.attributes.picture),
            userId = extractUserId(response.data.attributes.users_permissions_user)
        )
    }

    suspend fun createProfile(userId: Int, name: String, surname: String): Person? {
        val request = PersonRequest(
            data = PersonRequest.PersonData(
                name = name,
                surname = surname,
                users_permissions_user = userId
            )
        )

        val response = apiService.createPerson(request)

        return Person(
            id = response.data.id,
            name = name,
            surname = surname,
            imageUrl = obtenerUrlImagen(response.data.attributes.picture),
            userId = userId
        )
    }

    private fun obtenerUrlImagen(picture: Any?): String? {
        if (picture == null) return null

        try {
            if (picture is Map<*, *>) {
                val data = picture["data"]
                if (data is Map<*, *>) {
                    val attributes = data["attributes"]
                    if (attributes is Map<*, *>) {
                        return attributes["url"] as? String
                    }
                }
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun extractUserId(userPermissionsUser: Any?): Int {
        if (userPermissionsUser == null) return 0
        try {
            if (userPermissionsUser is Map<*, *>) {
                val data = userPermissionsUser["data"]
                if (data is Map<*, *>) {
                    return (data["id"] as? Number)?.toInt() ?: 0
                }
            }
            return 0
        } catch (e: Exception) {
            return 0
        }
    }
}