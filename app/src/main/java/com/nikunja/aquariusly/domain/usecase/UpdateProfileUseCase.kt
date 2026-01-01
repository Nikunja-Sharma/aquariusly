package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.model.UserProfile
import com.nikunja.aquariusly.domain.repository.UserRepository
import com.nikunja.aquariusly.domain.util.Resource
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String?, bio: String? = null): Resource<UserProfile> {
        return userRepository.updateProfile(name, bio)
    }
}
