package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.model.UserProfile
import com.nikunja.aquariusly.domain.repository.UserRepository
import com.nikunja.aquariusly.domain.util.Resource
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<UserProfile> {
        return userRepository.getProfile()
    }
}
