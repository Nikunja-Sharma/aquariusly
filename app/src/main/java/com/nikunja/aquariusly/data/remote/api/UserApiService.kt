package com.nikunja.aquariusly.data.remote.api

import com.nikunja.aquariusly.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @GET("user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @PUT("user/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<UserProfileResponse>

    @GET("user/preferences")
    suspend fun getPreferences(): Response<PreferencesResponse>

    @PUT("user/preferences")
    suspend fun updatePreferences(
        @Body request: UpdatePreferencesRequest
    ): Response<PreferencesResponse>

    @DELETE("user/account")
    suspend fun deleteAccount(): Response<MessageResponse>
}
