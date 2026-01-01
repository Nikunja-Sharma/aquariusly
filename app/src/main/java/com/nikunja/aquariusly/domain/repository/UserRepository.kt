package com.nikunja.aquariusly.domain.repository

import com.nikunja.aquariusly.domain.model.UserProfile
import com.nikunja.aquariusly.domain.model.UserPreferences
import com.nikunja.aquariusly.domain.util.Resource

interface UserRepository {
    suspend fun getProfile(): Resource<UserProfile>
    suspend fun updateProfile(name: String?, bio: String?): Resource<UserProfile>
    suspend fun getPreferences(): Resource<UserPreferences>
    suspend fun updatePreferences(theme: String?, notifications: Boolean?, language: String?): Resource<UserPreferences>
    suspend fun deleteAccount(): Resource<Unit>
}
