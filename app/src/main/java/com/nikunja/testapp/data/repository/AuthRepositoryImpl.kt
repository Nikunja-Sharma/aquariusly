package com.nikunja.testapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nikunja.testapp.domain.model.User
import com.nikunja.testapp.domain.repository.AuthRepository
import com.nikunja.testapp.domain.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Resource<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
            
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email.orEmpty(),
                    displayName = firebaseUser.displayName.orEmpty(),
                    photoUrl = firebaseUser.photoUrl?.toString()
                )
                Resource.Success(user)
            } else {
                Resource.Error("Sign in failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email.orEmpty(),
            displayName = firebaseUser.displayName.orEmpty(),
            photoUrl = firebaseUser.photoUrl?.toString()
        )
    }

    override suspend fun signOut(): Resource<Unit> {
        return try {
            firebaseAuth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sign out failed")
        }
    }

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
}
