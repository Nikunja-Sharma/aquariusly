package com.nikunja.aquariusly.domain.repository

import com.nikunja.aquariusly.domain.model.User
import com.nikunja.aquariusly.domain.util.Resource

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<User>
    fun getCurrentUser(): User?
    suspend fun signOut(): Resource<Unit>
    fun isUserLoggedIn(): Boolean
}
