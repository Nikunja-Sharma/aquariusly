package com.nikunja.testapp.domain.usecase

import com.nikunja.testapp.domain.repository.AuthRepository
import com.nikunja.testapp.domain.util.Resource
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> = authRepository.signOut()
}
