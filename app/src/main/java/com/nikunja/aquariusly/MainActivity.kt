package com.nikunja.aquariusly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.nikunja.aquariusly.ui.navigation.NavGraph
import com.nikunja.aquariusly.ui.theme.AquariuslyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val PREFS_NAME = "aquariusly_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Keep splash screen visible while loading initial data
        var keepSplashVisible = true
        splashScreen.setKeepOnScreenCondition { keepSplashVisible }
        
        enableEdgeToEdge()
        
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val hasCompletedOnboarding = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        
        // Handle deep link
        val deepLinkChatId = handleDeepLink(intent)
        
        setContent {
            var onboardingCompleted by remember { mutableStateOf(hasCompletedOnboarding) }
            var initialChatId by remember { mutableStateOf(deepLinkChatId) }
            
            // Dismiss splash after 5 seconds (for debugging - remove later)
            LaunchedEffect(Unit) {
                // kotlinx.coroutines.delay(5000L)
                keepSplashVisible = false
            }
            
            AquariuslyTheme {
                val navController = rememberNavController()
                
                NavGraph(
                    navController = navController,
                    hasCompletedOnboarding = onboardingCompleted,
                    initialChatId = initialChatId
                )
                
                // Clear initial chat id after first composition
                LaunchedEffect(Unit) {
                    initialChatId = null
                }
                
                // Save onboarding state when navigation changes
                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        if (destination.route != "onboarding" && !onboardingCompleted) {
                            prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
                            onboardingCompleted = true
                        }
                    }
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle deep link when app is already running
        handleDeepLink(intent)?.let { chatId ->
            // You can use a shared ViewModel or event bus to notify the chat screen
            // For now, we'll recreate the activity
            setIntent(intent)
            recreate()
        }
    }
    
    private fun handleDeepLink(intent: Intent?): String? {
        intent?.data?.let { uri ->
            // Handle aquariusly://chat/{chatId}
            if (uri.scheme == "aquariusly" && uri.host == "chat") {
                return uri.lastPathSegment
            }
            // Handle https://ai.isdev.in/chat/{chatId}
            if (uri.scheme == "https" && uri.host == "ai.isdev.in" && uri.path?.startsWith("/chat/") == true) {
                return uri.lastPathSegment
            }
        }
        return null
    }
}
