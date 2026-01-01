package com.nikunja.aquariusly.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshInterceptor @Inject constructor(
    private val cookieInterceptorProvider: () -> CookieInterceptor
) : Interceptor {

    @Volatile
    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // If we get 401 and it's not a refresh request, try to refresh
        if (response.code == 401 && !request.url.encodedPath.contains("auth/refresh")) {
            synchronized(this) {
                if (!isRefreshing) {
                    isRefreshing = true
                    response.close()

                    val refreshed = tryRefreshToken(chain)
                    isRefreshing = false

                    if (refreshed) {
                        // Retry original request with new token
                        return chain.proceed(request.newBuilder().build())
                    } else {
                        // Clear cookies on refresh failure
                        cookieInterceptorProvider().clearCookies()
                    }
                }
            }
        }

        return response
    }

    private fun tryRefreshToken(chain: Interceptor.Chain): Boolean {
        return try {
            val refreshRequest = Request.Builder()
                .url(chain.request().url.newBuilder()
                    .encodedPath("/api/auth/refresh")
                    .build())
                .post(okhttp3.RequestBody.create(null, ByteArray(0)))
                .build()

            val refreshResponse = chain.proceed(refreshRequest)
            refreshResponse.isSuccessful.also { refreshResponse.close() }
        } catch (e: Exception) {
            false
        }
    }
}
