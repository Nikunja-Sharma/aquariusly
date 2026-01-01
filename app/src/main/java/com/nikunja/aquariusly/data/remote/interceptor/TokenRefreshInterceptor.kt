package com.nikunja.aquariusly.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshInterceptor @Inject constructor(
    private val cookieInterceptorProvider: () -> CookieInterceptor
) : Interceptor {

    @Volatile
    private var isRefreshing = false
    
    private val lock = Any()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // If we get 401 and it's not a refresh request, try to refresh
        if (response.code == 401 && !request.url.encodedPath.contains("auth/refresh")) {
            synchronized(lock) {
                // Double-check after acquiring lock
                if (isRefreshing) {
                    // Another thread is refreshing, wait and retry with new cookies
                    response.close()
                    return retryWithNewCookies(chain, request)
                }
                
                isRefreshing = true
                response.close()

                return try {
                    val refreshed = tryRefreshToken(chain)
                    
                    if (refreshed) {
                        // Retry original request with new cookies
                        retryWithNewCookies(chain, request)
                    } else {
                        // Clear cookies on refresh failure
                        cookieInterceptorProvider().clearCookies()
                        // Return a new 401 response to signal auth failure
                        chain.proceed(request)
                    }
                } finally {
                    isRefreshing = false
                }
            }
        }

        return response
    }
    
    private fun retryWithNewCookies(chain: Interceptor.Chain, originalRequest: Request): Response {
        val cookieInterceptor = cookieInterceptorProvider()
        val cookies = cookieInterceptor.getCookies()
        
        val retryRequest = originalRequest.newBuilder()
            .apply {
                if (cookies.isNotEmpty()) {
                    header("Cookie", cookies)
                }
            }
            .build()
        
        return chain.proceed(retryRequest)
    }

    private fun tryRefreshToken(chain: Interceptor.Chain): Boolean {
        return try {
            val cookieInterceptor = cookieInterceptorProvider()
            val cookies = cookieInterceptor.getCookies()
            
            val refreshRequest = Request.Builder()
                .url(chain.request().url.newBuilder()
                    .encodedPath("/api/auth/refresh")
                    .build())
                .post("".toRequestBody("application/json".toMediaType()))
                .header("Content-Type", "application/json")
                .apply {
                    if (cookies.isNotEmpty()) {
                        header("Cookie", cookies)
                    }
                }
                .build()

            val refreshResponse = chain.proceed(refreshRequest)
            val isSuccessful = refreshResponse.isSuccessful
            
            // Extract and save cookies from refresh response
            if (isSuccessful) {
                refreshResponse.headers("Set-Cookie").forEach { cookie ->
                    cookieInterceptor.saveCookieFromHeader(cookie)
                }
            }
            
            refreshResponse.close()
            isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    
    private fun saveCookieFromHeader(setCookieHeader: String, cookieInterceptor: CookieInterceptor) {
        // The CookieInterceptor will handle this on the next request,
        // but we need to trigger a save for the retry
        // This is handled by the CookieInterceptor's intercept method
    }
}
