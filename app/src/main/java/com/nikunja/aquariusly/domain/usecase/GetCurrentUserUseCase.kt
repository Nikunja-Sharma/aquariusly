package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.model.User
import com.nikunja.aquariusly.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? = authRepository.getCurrentUser()
}
