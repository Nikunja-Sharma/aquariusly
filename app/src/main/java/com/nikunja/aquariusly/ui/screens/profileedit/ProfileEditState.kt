package com.nikunja.aquariusly.ui.screens.profileedit

import com.nikunja.aquariusly.domain.model.UserProfile

data class ProfileEditState(
    val profile: UserProfile? = null,
    val editedName: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)
