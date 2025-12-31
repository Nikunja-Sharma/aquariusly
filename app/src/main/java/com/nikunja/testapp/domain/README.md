# Domain Layer

The domain layer contains the core business logic for Flavor. It is completely independent of any framework or external library, making it highly testable and reusable.

## Structure

```
domain/
├── model/           # Business models
├── repository/      # Repository interfaces (contracts)
├── usecase/         # Business logic use cases
└── util/            # Domain utilities
```

## Components

### Models

#### Item
Core business model representing a recipe:
```kotlin
data class Item(
    val id: Int,
    val title: String,
    val description: String
)
```

#### User
Authenticated user model:
```kotlin
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?
)
```

### Repository Interfaces

#### AuthRepository
Authentication contract:
- `signInWithGoogle(idToken)` - Authenticate with Google
- `getCurrentUser()` - Get current authenticated user
- `signOut()` - Sign out current user
- `isUserLoggedIn()` - Check authentication status

#### ItemRepository
Item data contract:
- `getItems()` - Flow of items with Resource wrapper
- `refreshItems()` - Force refresh from remote

### Use Cases

Each use case represents a single business action:

| Use Case | Description |
|----------|-------------|
| `GetCurrentUserUseCase` | Retrieves the currently authenticated user |
| `GetItemsUseCase` | Observes recipes from repository |
| `RefreshItemsUseCase` | Triggers recipe data refresh |
| `SignInWithGoogleUseCase` | Handles Google authentication |
| `SignOutUseCase` | Signs out the current user |

### Utilities

#### Resource
Sealed class for representing operation states:
```kotlin
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
```

## Design Principles

- **No Framework Dependencies**: Pure Kotlin code only
- **Single Responsibility**: Each use case does one thing
- **Dependency Inversion**: Depends on abstractions (interfaces)
- **Testability**: Easy to unit test with mock repositories

## Usage Pattern

```kotlin
// ViewModel injects use case
class HomeViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {
    
    init {
        viewModelScope.launch {
            getItemsUseCase().collect { result ->
                // Handle result
            }
        }
    }
}
```
