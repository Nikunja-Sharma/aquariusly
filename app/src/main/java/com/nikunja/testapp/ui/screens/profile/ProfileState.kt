package com.nikunja.testapp.ui.screens.profile

import com.nikunja.testapp.domain.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSignedOut: Boolean = false
)
