package com.nikunja.aquariusly.data.remote.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() {

    private var cachedToken: String? = null

    suspend fun getToken(): String? {
        return try {
            cachedToken ?: FirebaseMessaging.getInstance().token.await().also {
                cachedToken = it
                Log.d(TAG, "FCM Token: $it")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get FCM token", e)
            null
        }
    }

    fun onNewToken(token: String) {
        cachedToken = token
        Log.d(TAG, "New FCM token: $token")
        // Token will be sent to backend when user logs in
    }

    fun clearToken() {
        cachedToken = null
    }

    companion object {
        private const val TAG = "FcmTokenManager"
    }
}
