package com.nikunja.testapp.ui.screens.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val isCompleted: Boolean = false
)

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "All AI Models\nOne Place",
        description = "Access ChatGPT, Claude, Gemini, Grok, and more — all from a single, beautiful interface.",
        emoji = "🤖"
    ),
    OnboardingPage(
        title = "Switch Models\nInstantly",
        description = "Compare responses, find the best AI for your task. No more juggling between apps.",
        emoji = "⚡"
    ),
    OnboardingPage(
        title = "Premium\nExperience",
        description = "Enjoy a sleek, distraction-free interface designed for productivity and creativity.",
        emoji = "✨"
    )
)
