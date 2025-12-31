# UI Layer

The presentation layer for Flavor, built entirely with Jetpack Compose following MVVM pattern. All UI components are stateless and receive state from ViewModels via StateFlow.

## Structure

```
ui/
├── navigation/      # Compose Navigation setup
├── screens/         # Feature screens
│   ├── home/        # Home screen (recipe list)
│   ├── login/       # Login screen (Google Sign-In)
│   └── profile/     # Profile screen (user info)
└── theme/           # Flavor Material 3 theme
```

## Navigation

### Screen Routes
```kotlin
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}
```

### NavGraph
- Determines start destination based on auth state
- Handles navigation events from screens
- Manages back stack properly on auth changes

## Screens

### Login Screen
- Google Sign-In via Credential Manager API
- Loading state during authentication
- Error handling with Snackbar
- Auto-navigates on successful login

### Home Screen
- Displays recipes in LazyColumn
- Pull-to-refresh functionality
- Empty state and error handling
- Profile navigation via TopAppBar action

### Profile Screen
- Displays user info (photo, name, email)
- Sign-out functionality
- Auto-navigates to login on sign-out

## State Management

Each screen follows the same pattern:

```kotlin
// State data class
data class FeatureState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

// ViewModel exposes StateFlow
class FeatureViewModel : ViewModel() {
    private val _state = MutableStateFlow(FeatureState())
    val state: StateFlow<FeatureState> = _state.asStateFlow()
}

// Composable observes state
@Composable
fun FeatureScreen(viewModel: FeatureViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Render based on state
}
```

## Theme

### FlavorTheme
Flavor's Material 3 theme with:
- Light and dark color schemes
- Dynamic color support (Android 12+)
- Custom typography scale
- Rounded corner shapes

### Colors
Full Material 3 color palette including:
- Primary, Secondary, Tertiary
- Error colors
- Surface and background variants
- Light and dark variants

## Key Patterns

- **Stateless Composables**: UI only reads state, never modifies
- **Unidirectional Data Flow**: Events → ViewModel → State → UI
- **Lifecycle-Aware Collection**: `collectAsStateWithLifecycle()`
- **Hilt Integration**: `hiltViewModel()` for DI
- **Material 3**: Latest design system components
