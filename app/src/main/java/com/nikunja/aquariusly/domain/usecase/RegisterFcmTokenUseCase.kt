package com.nikunja.aquariusly.domain.usecase

import android.util.Log
import com.nikunja.aquariusly.domain.repository.NotificationRepository
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        Log.d("RegisterFcmToken", "Attempting to get FCM token...")
        val token = notificationRepository.getFcmToken()
        
        if (token == null) {
            Log.e("RegisterFcmToken", "Failed to get FCM token")
            return Result.failure(Exception("Failed to get FCM token"))
        }
        
        Log.d("RegisterFcmToken", "Got token, registering with backend...")
        return notificationRepository.registerFcmToken(token)
    }
}
