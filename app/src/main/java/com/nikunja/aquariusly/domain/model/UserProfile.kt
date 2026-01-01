package com.nikunja.aquariusly.domain.model

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
    val bio: String?,
    val preferences: UserPreferences
)

data class UserPreferences(
    val theme: String,
    val notifications: Boolean,
    val language: String
)
