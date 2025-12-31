package com.nikunja.testapp.domain.repository

import com.nikunja.testapp.domain.model.User
import com.nikunja.testapp.domain.util.Resource

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<User>
    fun getCurrentUser(): User?
    suspend fun signOut(): Resource<Unit>
    fun isUserLoggedIn(): Boolean
}
