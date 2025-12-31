package com.nikunja.aquariusly.ui.screens.home

import com.nikunja.aquariusly.domain.model.Item

data class HomeState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null
)
