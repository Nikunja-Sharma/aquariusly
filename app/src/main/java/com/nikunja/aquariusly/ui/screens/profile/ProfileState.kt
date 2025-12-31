package com.nikunja.aquariusly.ui.screens.profile

import com.nikunja.aquariusly.domain.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSignedOut: Boolean = false
)
