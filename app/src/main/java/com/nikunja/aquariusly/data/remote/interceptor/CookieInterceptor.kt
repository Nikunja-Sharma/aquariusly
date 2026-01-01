package com.nikunja.aquariusly.data.remote.interceptor

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "auth_cookies",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add cookies to request
        val cookies = getCookies()
        val requestBuilder = originalRequest.newBuilder()
        if (cookies.isNotEmpty()) {
            requestBuilder.header("Cookie", cookies)
        }

        val response = chain.proceed(requestBuilder.build())

        // Save cookies from response
        response.headers("Set-Cookie").forEach { cookie ->
            saveCookie(cookie)
        }

        return response
    }

    private fun getCookies(): String {
        val accessToken = prefs.getString(KEY_ACCESS_TOKEN, null)
        val refreshToken = prefs.getString(KEY_REFRESH_TOKEN, null)

        val cookies = mutableListOf<String>()
        accessToken?.let { cookies.add("access_token=$it") }
        refreshToken?.let { cookies.add("refresh_token=$it") }

        return cookies.joinToString("; ")
    }

    private fun saveCookie(setCookieHeader: String) {
        val parts = setCookieHeader.split(";").first().split("=", limit = 2)
        if (parts.size == 2) {
            val name = parts[0].trim()
            val value = parts[1].trim()

            when (name) {
                "access_token" -> prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()
                "refresh_token" -> prefs.edit().putString(KEY_REFRESH_TOKEN, value).apply()
            }
        }
    }

    fun clearCookies() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    fun hasValidSession(): Boolean {
        return prefs.getString(KEY_ACCESS_TOKEN, null) != null
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
