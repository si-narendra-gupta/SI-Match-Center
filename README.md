# SI Match Center Library Integration Guide

This guide provides step-by-step instructions on how to integrate the `si_matchcenter` library into your Android application.

## Prerequisites

Before you begin, ensure your project meets the following requirements:
*   **Java Version**: JDK 17 or higher.
*   **Android Gradle Plugin**: Version 9.x or higher.
*   **Compose**: Jetpack Compose enabled.
*   **Hilt**: Dagger Hilt configured for dependency injection.

## Step 1: Add Repository and Dependency

First, add the JitPack repository to your `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then, add the library dependency to your application's `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("com.github.si-narendra-gupta:SI-Match-Center:1.0.0")
    
    // Hilt (Version 2.60)
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
}
```

## Step 2: Initialize Hilt

Your `Application` class must be annotated with `@HiltAndroidApp`:

```kotlin
// In your Application class
@HiltAndroidApp
class SIMatchCenterApp : Application()
```

## Step 3: Configure the Activity

Any Activity that hosts the Match Center must be annotated with `@AndroidEntryPoint`:

```kotlin
// In your Activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Using the MatchCenterContent Composable
            MatchCenterContent(
                gameId = "rrck03302026267884", // Example Game ID
                theme = ThemeConstants.DEFAULT_THEME
            )
        }
    }
}
```

## Step 4: Component Usage

The main entry point is the `MatchCenterContent` composable.

### Signature:
```kotlin
@Composable
fun MatchCenterContent(
    viewModel: MatchCenterViewModel = hiltViewModel(), 
    gameId: String, 
    theme: String? = null
)
```

### Parameters:
*   `gameId`: The unique identifier for the match data to be displayed.
*   `theme`: (Optional) A string identifier for applying specific theme colors. If omitted, the default theme is used.

## Step 5: Theming

The library supports dynamic theming. You can pass theme constants from `ThemeConstants` (e.g., `THEME_PINK_NAVY`, `DEFAULT_THEME`).

---
*Developed by Sportz Interactive*
