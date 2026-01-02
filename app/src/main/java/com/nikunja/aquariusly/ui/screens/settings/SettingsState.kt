package com.nikunja.aquariusly.ui.screens.settings

import com.nikunja.aquariusly.domain.model.UserProfile

data class SettingsState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val isDarkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val hapticFeedback: Boolean = true,
    val error: String? = null,
    val isSigningOut: Boolean = false,
    val isSignedOut: Boolean = false
)
