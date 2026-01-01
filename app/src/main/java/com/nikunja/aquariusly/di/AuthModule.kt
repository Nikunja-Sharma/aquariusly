package com.nikunja.aquariusly.di

import com.nikunja.aquariusly.data.remote.api.AuthApiService
import com.nikunja.aquariusly.data.remote.interceptor.CookieInterceptor
import com.nikunja.aquariusly.data.repository.AuthRepositoryImpl
import com.nikunja.aquariusly.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        cookieInterceptor: CookieInterceptor
    ): AuthRepository {
        return AuthRepositoryImpl(authApiService, cookieInterceptor)
    }
}
