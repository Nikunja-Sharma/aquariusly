package com.nikunja.aquariusly.data.repository

import com.nikunja.aquariusly.data.remote.api.UserApiService
import com.nikunja.aquariusly.data.remote.dto.UpdatePreferencesRequest
import com.nikunja.aquariusly.data.remote.dto.UpdateProfileRequest
import com.nikunja.aquariusly.data.remote.dto.UserPreferencesDto
import com.nikunja.aquariusly.data.remote.dto.UserProfileDto
import com.nikunja.aquariusly.domain.model.UserPreferences
import com.nikunja.aquariusly.domain.model.UserProfile
import com.nikunja.aquariusly.domain.repository.UserRepository
import com.nikunja.aquariusly.domain.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getProfile(): Resource<UserProfile> {
        return try {
            val response = userApiService.getProfile()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body.profile.toDomain())
                } else {
                    Resource.Error("Empty response")
                }
            } else {
                Resource.Error(response.message() ?: "Failed to get profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    override suspend fun updateProfile(name: String?, bio: String?): Resource<UserProfile> {
        return try {
            val response = userApiService.updateProfile(UpdateProfileRequest(name, bio))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body.profile.toDomain())
                } else {
                    Resource.Error("Empty response")
                }
            } else {
                Resource.Error(response.message() ?: "Failed to update profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    override suspend fun getPreferences(): Resource<UserPreferences> {
        return try {
            val response = userApiService.getPreferences()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body.preferences.toDomain())
                } else {
                    Resource.Error("Empty response")
                }
            } else {
                Resource.Error(response.message() ?: "Failed to get preferences")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    override suspend fun updatePreferences(
        theme: String?,
        notifications: Boolean?,
        language: String?
    ): Resource<UserPreferences> {
        return try {
            val response = userApiService.updatePreferences(
                UpdatePreferencesRequest(theme, notifications, language)
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body.preferences.toDomain())
                } else {
                    Resource.Error("Empty response")
                }
            } else {
                Resource.Error(response.message() ?: "Failed to update preferences")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    override suspend fun deleteAccount(): Resource<Unit> {
        return try {
            val response = userApiService.deleteAccount()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message() ?: "Failed to delete account")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    private fun UserProfileDto.toDomain(): UserProfile {
        return UserProfile(
            id = id,
            email = email,
            name = name,
            picture = picture,
            bio = bio,
            preferences = preferences.toDomain()
        )
    }

    private fun UserPreferencesDto.toDomain(): UserPreferences {
        return UserPreferences(
            theme = theme,
            notifications = notifications,
            language = language
        )
    }
}
