package com.nikunja.testapp.ui.screens.home

import com.nikunja.testapp.domain.model.Item

data class HomeState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null
)
