package com.nikunja.testapp.domain.usecase

import com.nikunja.testapp.domain.model.User
import com.nikunja.testapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? = authRepository.getCurrentUser()
}
