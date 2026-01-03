package com.nikunja.aquariusly.data.remote.api

import com.nikunja.aquariusly.data.remote.dto.FcmTokenRequest
import com.nikunja.aquariusly.data.remote.dto.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface NotificationApiService {

    @POST("notifications/fcm-token")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRequest
    ): Response<MessageResponse>

    @DELETE("notifications/fcm-token")
    suspend fun unregisterFcmToken(): Response<MessageResponse>
}
