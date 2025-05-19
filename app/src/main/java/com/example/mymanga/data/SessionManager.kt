package com.example.mymanga.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    
    private var userToken: String? = null
    private var userId: Int = -1
    private var userName: String? = null
    private var userEmail: String? = null

    fun saveAuthUser(token: String, user: User) {
        userToken = token
        userId = user.id
        userName = user.username
        userEmail = user.email
    }

    fun fetchAuthToken(): String? {
        return userToken
    }

    fun getUserId(): Int {
        return userId
    }

    fun getUserName(): String? {
        return userName
    }

    fun getUserEmail(): String? {
        return userEmail
    }

    fun clearSession() {
        userToken = null
        userId = -1
        userName = null
        userEmail = null
    }

    fun isLoggedIn(): Boolean {
        return userToken != null
    }
}