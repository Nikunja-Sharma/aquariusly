package com.nikunja.aquariusly.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Chat : Screen("chat")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object ModelSelection : Screen("model_selection")
    data object Settings : Screen("settings")
    data object ProfileEdit : Screen("profile_edit")
}
