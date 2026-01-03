package com.nikunja.aquariusly.di

import com.nikunja.aquariusly.data.remote.api.NotificationApiService
import com.nikunja.aquariusly.data.remote.messaging.FcmTokenManager
import com.nikunja.aquariusly.data.repository.NotificationRepositoryImpl
import com.nikunja.aquariusly.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationApiService: NotificationApiService,
        fcmTokenManager: FcmTokenManager
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationApiService, fcmTokenManager)
    }
}
