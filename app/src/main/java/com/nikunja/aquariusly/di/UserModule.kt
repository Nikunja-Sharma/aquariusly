package com.nikunja.aquariusly.di

import com.nikunja.aquariusly.data.remote.api.UserApiService
import com.nikunja.aquariusly.data.repository.UserRepositoryImpl
import com.nikunja.aquariusly.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService
    ): UserRepository {
        return UserRepositoryImpl(userApiService)
    }
}
