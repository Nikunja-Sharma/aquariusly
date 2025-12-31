package com.nikunja.testapp.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Chat : Screen("chat")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object ModelSelection : Screen("model_selection")
}
