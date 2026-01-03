package com.nikunja.aquariusly.domain.repository

interface NotificationRepository {
    suspend fun getFcmToken(): String?
    suspend fun registerFcmToken(token: String): Result<Unit>
    suspend fun unregisterFcmToken(): Result<Unit>
}
