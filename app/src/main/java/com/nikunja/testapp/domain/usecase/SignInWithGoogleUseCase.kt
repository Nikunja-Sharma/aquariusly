package com.nikunja.testapp.domain.usecase

import com.nikunja.testapp.domain.model.User
import com.nikunja.testapp.domain.repository.AuthRepository
import com.nikunja.testapp.domain.util.Resource
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Resource<User> {
        return authRepository.signInWithGoogle(idToken)
    }
}
