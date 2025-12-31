package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.repository.AuthRepository
import com.nikunja.aquariusly.domain.util.Resource
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> = authRepository.signOut()
}
