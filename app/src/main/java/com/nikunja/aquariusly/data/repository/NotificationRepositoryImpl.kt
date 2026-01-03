package com.nikunja.aquariusly.data.repository

import com.nikunja.aquariusly.data.remote.api.NotificationApiService
import com.nikunja.aquariusly.data.remote.dto.FcmTokenRequest
import com.nikunja.aquariusly.data.remote.messaging.FcmTokenManager
import com.nikunja.aquariusly.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService,
    private val fcmTokenManager: FcmTokenManager
) : NotificationRepository {

    override suspend fun getFcmToken(): String? {
        return fcmTokenManager.getToken()
    }

    override suspend fun registerFcmToken(token: String): Result<Unit> {
        return try {
            val response = notificationApiService.registerFcmToken(FcmTokenRequest(token))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to register FCM token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unregisterFcmToken(): Result<Unit> {
        return try {
            val response = notificationApiService.unregisterFcmToken()
            if (response.isSuccessful) {
                fcmTokenManager.clearToken()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to unregister FCM token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
